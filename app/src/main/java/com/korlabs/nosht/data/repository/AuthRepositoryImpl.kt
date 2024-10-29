package com.korlabs.nosht.data.repository

import android.util.Log
import com.korlabs.nosht.NoshtApplication
import com.korlabs.nosht.R
import com.korlabs.nosht.data.mapper.toBusiness
import com.korlabs.nosht.data.mapper.toBusinessEntity
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.domain.remote.AuthClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.domain.repository.AuthRepository
import com.korlabs.nosht.data.local.NoshtDatabase
import com.korlabs.nosht.data.mapper.toEmployer
import com.korlabs.nosht.data.mapper.toEmployerEntity
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.domain.model.users.User
import com.korlabs.nosht.util.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authClient: AuthClient,
    private val apiClient: APIClient,
    localDB: NoshtDatabase
) : AuthRepository {

    companion object {
        var currentBusinessUid: String? = null
        var currentUser: User? = null
    }

    private val dao = localDB.dao

    override suspend fun login(user: String, password: String): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading(true))

            val response = authClient.login(user, password)

            if (response is Resource.Successful) {
                val responseUserLogged = apiClient.getUser(response.data)
                if (responseUserLogged is Resource.Successful) {
                    currentUser = responseUserLogged.data

                    if (currentUser!!.typeUserEnum == TypeUserEnum.BUSINESS) {
                        Log.d(Util.TAG, "Is a business ${currentUser!! as Business}")

                        dao.loginBusiness((currentUser!! as Business).toBusinessEntity())
                        currentBusinessUid = (currentUser as Business).uid

                        emit(Resource.Successful(data = dao.getBusiness()?.toBusiness() as User))
                    } else {
                        Log.d(Util.TAG, "Is a employer ${currentUser!! as Employer}")

                        dao.loginEmployer((currentUser!! as Employer).toEmployerEntity())

                        emit(Resource.Successful(data = dao.getEmployer()?.toEmployer() as User))
                    }
                } else {
                    emit(
                        Resource.Error(
                            responseUserLogged.message
                                ?: NoshtApplication.appContext.getString(R.string.unrecognized_error)
                        )
                    )
                }
            } else {
                emit(
                    Resource.Error(
                        response.message
                            ?: NoshtApplication.appContext.getString(R.string.unrecognized_error)
                    )
                )
            }
        }.catch {
            emit(Resource.Error(NoshtApplication.appContext.getString(R.string.unrecognized_error)))
        }
    }

    override suspend fun signUp(userSignUp: UserSignUp): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading(true))

            val response = authClient.signUp(userSignUp)

            if (response is Resource.Successful) {
                Log.d(Util.TAG, "Successful signUp weon")
                val resultResource = apiClient.createUser(userSignUp)
                emit(resultResource)
            } else {
                Log.d(Util.TAG, "Failed signup weon")
                emit(response)
            }
        }.catch { exception ->
            Log.e(Util.TAG, "Unrecognized error $exception")
            emit(
                Resource.Error(
                    exception.message
                        ?: NoshtApplication.appContext.getString(R.string.unrecognized_error)
                )
            )
        }
    }

    override suspend fun getLoggedUser(): User? {
        currentUser = dao.getBusiness()?.toBusiness()

        if (currentUser != null) {
            Log.d(Util.TAG, "Is a business ${currentUser!! as Business}")
            currentBusinessUid = (currentUser as Business).uid
            return currentUser
        }

        currentUser = dao.getEmployer()?.toEmployer()

        if (currentUser != null) {
            Log.d(Util.TAG, "Is an employer ${currentUser!! as Employer}")
            return currentUser
        }

        return null
    }
}