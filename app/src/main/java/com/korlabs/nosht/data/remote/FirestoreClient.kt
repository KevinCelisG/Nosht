package com.korlabs.nosht.data.remote


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.korlabs.nosht.NoshtApplication
import com.korlabs.nosht.R
import com.korlabs.nosht.data.mapper.toBusiness
import com.korlabs.nosht.data.mapper.toEmployer
import com.korlabs.nosht.data.remote.model.UserSignUp
import com.korlabs.nosht.domain.model.Contract
import com.korlabs.nosht.domain.model.Menu
import com.korlabs.nosht.domain.model.MenusWithAmountInOrder
import com.korlabs.nosht.domain.model.Order
import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceMovement
import com.korlabs.nosht.domain.model.ResourceWithAmountInMenu
import com.korlabs.nosht.domain.model.Table
import com.korlabs.nosht.domain.model.enums.TableStatusEnum
import com.korlabs.nosht.domain.model.enums.TypeUserEnum
import com.korlabs.nosht.domain.model.enums.employee.CodeStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.EmployerStatusEnum
import com.korlabs.nosht.domain.model.enums.employee.TypeEmployeeRoleEnum
import com.korlabs.nosht.domain.model.users.Business
import com.korlabs.nosht.domain.model.users.Employer
import com.korlabs.nosht.domain.model.users.User
import com.korlabs.nosht.domain.remote.APIClient
import com.korlabs.nosht.util.Resource
import com.korlabs.nosht.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreClient @Inject constructor() : APIClient {

    private val businessCollection = "Business"
    private val codesCollection = "Codes"
    private val employersCollection = "Employers"

    private val historyCollection = "History"
    private val tablesCollection = "Tables"

    private val ordersCollection = "Orders"
    private val itemsOrderCollection = "Items"
    private val menusOrderCollection = "Menus"

    private val resourcesBusinessCollection = "Resources"
    private val resourcesMovementsBusinessCollection = "Movements"
    private val menusCollection = "Menus"
    private val componentsCollection = "Components"


    private lateinit var listenerEmployerResponse: ListenerRegistration

    private val firestore = Firebase.firestore

    private val _data = MutableLiveData<Resource<List<Table>>>()
    override val data: LiveData<Resource<List<Table>>> = _data

    private val _isJoinEmployer = MutableLiveData<Resource<String>>()
    override val isJoinEmployer: LiveData<Resource<String>> = _isJoinEmployer

    private val _dataContracts = MutableLiveData<Resource<List<Contract>>>()
    override val dataContracts: LiveData<Resource<List<Contract>>> = _dataContracts

    private val _dataResourcesBusiness = MutableLiveData<Resource<List<ResourceBusiness>>>()
    override val dataResourcesBusiness: LiveData<Resource<List<ResourceBusiness>>> =
        _dataResourcesBusiness

    private val _dataMenus = MutableLiveData<Resource<List<Menu>>>()
    override val dataMenus: LiveData<Resource<List<Menu>>> = _dataMenus

    private val _dataOrders = MutableLiveData<Resource<List<Order>>>()
    override val dataOrders: LiveData<Resource<List<Order>>> = _dataOrders

    companion object {
        var isNewTablesData = false
        var isNewResourcesData = false
        var isNewMenusData = false
        var isNewContractsData = false
        var isNewOrdersData = false

        var tablesListener: ListenerRegistration? = null
        var resourcesListener: ListenerRegistration? = null
        var menusListener: ListenerRegistration? = null
        var contractsListener: ListenerRegistration? = null
        var ordersListener: ListenerRegistration? = null

        fun stopNewData() {
            isNewTablesData = false
            isNewResourcesData = false
            isNewMenusData = false
            isNewContractsData = false
            isNewOrdersData = false

            Log.d(Util.TAG, "Stopped all new data")
        }

        fun stopListeners() {
            tablesListener?.remove()
            resourcesListener?.remove()
            menusListener?.remove()
            contractsListener?.remove()
            ordersListener?.remove()

            Log.d(Util.TAG, "Stopped all listening")
        }
    }

    override suspend fun createUser(userSignUp: UserSignUp): Resource<String> {
        return try {
            if (userSignUp.uid != null) {
                val userToCreate: HashMap<Any, Any>
                val collection: String

                if (userSignUp.typeUserEnum == TypeUserEnum.BUSINESS) {
                    userToCreate = hashMapOf(
                        "name" to userSignUp.name,
                        "lastName" to userSignUp.lastName,
                        "email" to userSignUp.email,
                        "phone" to userSignUp.phone,
                        "location" to userSignUp.location!!,
                        "businessName" to userSignUp.businessName!!,
                        "isOpenTheBusiness" to false,
                    )

                    collection = businessCollection
                } else {
                    userToCreate = hashMapOf(
                        "name" to userSignUp.name,
                        "lastName" to userSignUp.lastName,
                        "email" to userSignUp.email,
                        "phone" to userSignUp.phone,
                    )
                    collection = employersCollection
                }

                firestore.collection(collection)
                    .document(userSignUp.uid ?: "")
                    .set(userToCreate).await()

                Resource.Successful()
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getUser(uid: String?): Resource<User> {
        return try {
            if (uid != null) {
                var response = firestore.collection(businessCollection).document(uid).get().await()

                if (response.data != null) {
                    Log.d(Util.TAG, "Business data: ${response.data}")
                    Resource.Successful(response.toBusiness(uid))
                } else {
                    response = firestore.collection(employersCollection).document(uid).get().await()
                    if (response.data != null) {
                        Log.d(Util.TAG, "Employer data: ${response.data}")
                        Resource.Successful(response.toEmployer(uid))
                    } else {
                        Resource.Error(NoshtApplication.appContext.getString(R.string.no_such_account))
                    }
                }
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun addTable(currentBusiness: Business?, table: Table): Resource<Table> {
        return try {
            if (currentBusiness?.uid != null) {
                val tableData = hashMapOf(
                    "name" to table.name,
                    "status" to table.status.status
                )

                Log.d(Util.TAG, "Table data $tableData")

                val response = firestore.collection(businessCollection)
                    .document(currentBusiness.uid ?: "")
                    .collection(tablesCollection)
                    .add(tableData).await()

                table.documentReference = response.id

                Resource.Successful(table)
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getTables(businessUid: String) {
        val listTables = mutableListOf<Table>()

        tablesListener = firestore.collection(businessCollection)
            .document(businessUid)
            .collection(tablesCollection).addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    listTables.clear()

                    for (responseTable in snapshot.documents) {
                        val name = responseTable.getString("name")
                        val status = responseTable.getString("status")

                        if (name != null && status != null) {
                            listTables.add(
                                Table(name, Util.getTableStatus(status), responseTable.id)
                            )
                        }
                    }

                    Log.d(Util.TAG, "Update list tables data $listTables")
                    isNewTablesData = true
                    _data.value = Resource.Successful(listTables)
                }
            }
    }

    override suspend fun addResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness,
        resourceMovement: ResourceMovement
    ): Resource<ResourceBusiness> {
        return try {
            if (currentBusiness?.uid != null) {
                val movementPurchase = hashMapOf(
                    "date" to resourceMovement.date,
                    "amount" to resourceMovement.amount,
                    "price" to resourceMovement.price,
                    "typeMovement" to resourceMovement.typeMovement.type
                )

                var amount = resourceMovement.amount

                val response = firestore.collection(businessCollection)
                    .document(currentBusiness.uid ?: "")
                    .collection(resourcesBusinessCollection)
                    .document(resourceBusiness.documentReference!!)

                response.get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        amount += document.getLong("amount")!!

                        response.update("amount", amount).addOnSuccessListener {
                            Log.d(Util.TAG, "Amount updated, new value $amount")
                        }.addOnFailureListener {
                            Log.d(Util.TAG, "Amount NOT updated, new value $amount")
                        }
                    } else {
                        Log.d(
                            Util.TAG,
                            "The document of resource to add new resources doesn't exist"
                        )
                    }
                }.addOnFailureListener {
                    Log.d(
                        Util.TAG,
                        "Error getting the document of resource to add new resources doesn't exist"
                    )
                }

                response.collection(resourcesMovementsBusinessCollection).add(movementPurchase)
                    .await()

                resourceBusiness.amount = amount

                Resource.Successful(resourceBusiness)
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Log.d(Util.TAG, "Exception: $e")
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness
    ): Resource<Boolean> {
        return try {
            if (currentBusiness?.uid != null) {
                firestore.collection(businessCollection)
                    .document(currentBusiness.uid ?: "")
                    .collection(resourcesBusinessCollection)
                    .document(resourceBusiness.documentReference!!).delete().await()

                Resource.Successful(data = true)
            } else {
                Resource.Error(message = "Does not have UID", data = false)
            }
        } catch (e: Exception) {
            Log.d(Util.TAG, "Exception: $e")
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness
    ): Resource<ResourceBusiness> {
        return try {
            if (currentBusiness?.uid != null) {
                val tableData: Map<String, Any> = hashMapOf(
                    "name" to resourceBusiness.name,
                    "minimumStock" to resourceBusiness.minStock.toLong(),
                    "maximumStock" to resourceBusiness.maxStock.toLong(),
                    "price" to resourceBusiness.price,
                    "amount" to resourceBusiness.amount,
                    "typeResource" to resourceBusiness.typeResourceEnum.type,
                    "typeMeasurement" to resourceBusiness.typeMeasurementEnum.type
                )

                Log.d(Util.TAG, "Resource business data $tableData")
                Log.d(Util.TAG, "Updating the resource")

                firestore.collection(businessCollection)
                    .document(currentBusiness.uid ?: "")
                    .collection(resourcesBusinessCollection)
                    .document(resourceBusiness.documentReference!!).update(tableData).await()

                Resource.Successful(resourceBusiness)
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Log.d(Util.TAG, "Exception: $e")
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun createResourceBusiness(
        currentBusiness: Business?,
        resourceBusiness: ResourceBusiness
    ): Resource<ResourceBusiness> {
        return try {
            if (currentBusiness?.uid != null) {
                val tableData = hashMapOf(
                    "name" to resourceBusiness.name,
                    "minimumStock" to resourceBusiness.minStock.toLong(),
                    "maximumStock" to resourceBusiness.maxStock.toLong(),
                    "price" to resourceBusiness.price,
                    "amount" to resourceBusiness.amount,
                    "typeResource" to resourceBusiness.typeResourceEnum.type,
                    "typeMeasurement" to resourceBusiness.typeMeasurementEnum.type
                )

                Log.d(Util.TAG, "Resource business data $tableData")
                Log.d(Util.TAG, "Adding the resource")

                val response = firestore.collection(businessCollection)
                    .document(currentBusiness.uid ?: "")
                    .collection(resourcesBusinessCollection)
                    .add(tableData).await()

                resourceBusiness.documentReference = response.id

                Resource.Successful(resourceBusiness)
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Log.d(Util.TAG, "Exception: $e")
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getResourcesBusiness(businessUid: String) {
        val listResourcesBusiness = mutableListOf<ResourceBusiness>()

        resourcesListener = firestore.collection(businessCollection)
            .document(businessUid)
            .collection(resourcesBusinessCollection).addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    listResourcesBusiness.clear()

                    for (responseResource in snapshot.documents) {
                        val name = responseResource.getString("name")
                        val minimumStock = responseResource.getLong("minimumStock")
                        val maximumStock = responseResource.getLong("maximumStock")
                        val price = responseResource.getDouble("price")
                        val amount = responseResource.getDouble("amount")
                        val typeResource = responseResource.getString("typeResource")
                        val typeMeasurement = responseResource.getString("typeMeasurement")

                        if (name != null && minimumStock != null && maximumStock != null && price != null && amount != null && typeResource != null && typeMeasurement != null) {
                            listResourcesBusiness.add(
                                ResourceBusiness(
                                    name = name,
                                    minStock = minimumStock.toShort(),
                                    maxStock = maximumStock.toShort(),
                                    price = price.toFloat(),
                                    amount = amount.toFloat(),
                                    typeResourceEnum = Util.getTypeResource(typeResource),
                                    typeMeasurementEnum = Util.getTypeMeasurement(typeMeasurement),
                                    documentReference = responseResource.id
                                )
                            )
                        }
                    }

                    Log.d(Util.TAG, "Update list resource data $listResourcesBusiness")
                    isNewResourcesData = true
                    _dataResourcesBusiness.value = Resource.Successful(listResourcesBusiness)
                }
            }
    }

    override suspend fun addMenu(currentBusiness: Business?, menu: Menu): Resource<Menu> {
        return try {
            if (currentBusiness?.uid != null) {
                val menuData = hashMapOf(
                    "name" to menu.name,
                    "status" to menu.menuStatusEnum.status,
                    "price" to menu.price,
                    "isDynamic" to menu.isDynamic
                )

                Log.d(Util.TAG, "Menu data $menuData")

                val response = firestore.collection(businessCollection)
                    .document(currentBusiness.uid ?: "")
                    .collection(menusCollection)
                    .add(menuData).await()

                menu.documentReference = response.id

                for (resource in menu.listResourceBusiness) {
                    val resourceData = hashMapOf(
                        "documentReference" to resource.resourceBusiness.documentReference,
                        "amount" to resource.amount
                    )

                    firestore.collection(businessCollection)
                        .document(currentBusiness.uid ?: "")
                        .collection(menusCollection).document(response.id)
                        .collection(componentsCollection).add(resourceData).await()
                }

                Resource.Successful(menu)
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getMenus(businessUid: String) {
        menusListener = firestore.collection(businessCollection)
            .document(businessUid)
            .collection(menusCollection)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    // Lanzar una coroutine para manejar operaciones asincrónicas
                    CoroutineScope(Dispatchers.IO).launch {
                        val listMenus = mutableListOf<Menu>()

                        for (responseMenu in snapshot.documents) {
                            val name = responseMenu.getString("name")
                            val status = responseMenu.getString("status")
                            val price = responseMenu.get("price")
                            val isDynamic = responseMenu.getBoolean("isDynamic")

                            if (name != null && status != null && price != null && isDynamic != null) {
                                Log.d(Util.TAG, "All good with the fields")

                                // Obtener los componentes del menú
                                val resource =
                                    responseMenu.reference.collection(componentsCollection)
                                        .get()
                                        .await() // Utiliza await() para esperar a que la tarea se complete

                                val listResourceOfMenu = mutableListOf<ResourceWithAmountInMenu>()

                                // Iterar sobre los recursos del menú
                                for (currentResource in resource.documents) {
                                    if (currentResource != null) {
                                        val documentReference =
                                            currentResource.getString("documentReference")
                                        val amountResourceInMenu =
                                            currentResource.getLong("amount")!!.toFloat()

                                        if (documentReference != null) {
                                            var componentResourceOfMenu: ResourceBusiness? = null

                                            // Obtener los datos del recurso
                                            val resourceOfMenu =
                                                firestore.collection(businessCollection)
                                                    .document(businessUid)
                                                    .collection(resourcesBusinessCollection)
                                                    .document(documentReference)
                                                    .get()
                                                    .await() // Utiliza await() para esperar a que la tarea se complete

                                            Log.d(
                                                Util.TAG,
                                                "----------------- Getting menu components $resourceOfMenu"
                                            )

                                            val nameResource = resourceOfMenu.getString("name")
                                            val minimumStockResource =
                                                resourceOfMenu.getLong("minimumStock")
                                            val maximumStockResource =
                                                resourceOfMenu.getLong("maximumStock")
                                            val priceResource = resourceOfMenu.getDouble("price")
                                            val amountResource = resourceOfMenu.getDouble("amount")
                                            val typeResource =
                                                resourceOfMenu.getString("typeResource")
                                            val typeMeasurement =
                                                resourceOfMenu.getString("typeMeasurement")

                                            if (nameResource != null && minimumStockResource != null && maximumStockResource != null && priceResource != null && amountResource != null && typeResource != null && typeMeasurement != null) {
                                                componentResourceOfMenu = ResourceBusiness(
                                                    name = nameResource,
                                                    minStock = minimumStockResource.toShort(),
                                                    maxStock = maximumStockResource.toShort(),
                                                    price = priceResource.toFloat(),
                                                    amount = amountResource.toFloat(),
                                                    typeResourceEnum = Util.getTypeResource(
                                                        typeResource
                                                    ),
                                                    typeMeasurementEnum = Util.getTypeMeasurement(
                                                        typeMeasurement
                                                    ),
                                                    documentReference = resourceOfMenu.id
                                                )

                                                Log.d(
                                                    Util.TAG,
                                                    "Create the resource of menu $name: Resource $componentResourceOfMenu"
                                                )
                                            }

                                            if (componentResourceOfMenu != null) {
                                                listResourceOfMenu.add(
                                                    ResourceWithAmountInMenu(
                                                        componentResourceOfMenu,
                                                        amountResourceInMenu
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                                listMenus.add(
                                    Menu(
                                        name,
                                        listResourceOfMenu,
                                        Util.getStatusMenu(status),
                                        price.toString().toFloat(),
                                        isDynamic,
                                        responseMenu.id
                                    )
                                )

                                Log.d(Util.TAG, "Components menu: $listResourceOfMenu")
                            }
                        }

                        Log.d(Util.TAG, "Update menus data: $listMenus")
                        // Actualiza el estado en el hilo principal
                        withContext(Dispatchers.Main) {
                            isNewMenusData = true
                            _dataMenus.value = Resource.Successful(listMenus)
                        }
                    }
                }
            }
    }

    override suspend fun addEmployer(
        business: Business,
        employeeRoleEnum: TypeEmployeeRoleEnum,
        code: String
    ): Resource<String> {
        return try {
            if (business.uid != null) {
                val requestEmployer = hashMapOf(
                    "businessUid" to business.uid,
                    "status" to CodeStatusEnum.AVAILABLE.status,
                    "role" to employeeRoleEnum.role
                )

                firestore.collection(codesCollection).document(code).set(requestEmployer)
                    .await()

                Resource.Successful()
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun disabilityCode(business: Business, code: String) {
        firestore.collection(codesCollection).document(code).delete().await()
    }

    override suspend fun validateCode(employer: Employer, code: String): String? {
        val response = firestore.collection(codesCollection).document(code).get().await()
        return if (response.data != null) {
            firestore.collection(codesCollection).document(code).update(
                "status", CodeStatusEnum.SUCCESSFULLY_TAKEN.status,
                "employerUid", employer.uid!!
            ).await()

            Log.d(
                Util.TAG,
                "Successfully ----- Data: ${response.getString("role")} - ${response.getString("businessUid")} - ${
                    response.getString("status")
                }"
            )

            val businessUid = response.getString("businessUid")!!

            addMatchHistory(
                employer,
                businessUid,
                Util.getEmployerRole(response.getString("role")!!)
            )

            businessUid
        } else {
            Log.d(Util.TAG, "No such document")
            null
        }
    }

    override suspend fun listenEmployerResponse(business: Business, code: String) {
        listenerEmployerResponse = firestore.collection(codesCollection).document(code)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    if (snapshot.getString("status") != null) {
                        if (Util.getCodeStatus(snapshot.getString("status")!!) == CodeStatusEnum.SUCCESSFULLY_TAKEN) {
                            val uidEmployer = snapshot.getString("employerUid")
                            val role = snapshot.getString("role")

                            if (uidEmployer != null) {
                                Log.d(Util.TAG, "Employer joined $uidEmployer")

                                addMatchHistory(
                                    business,
                                    uidEmployer,
                                    Util.getEmployerRole(role!!)
                                )

                                _isJoinEmployer.value = Resource.Successful("Successful")

                                listenerEmployerResponse.remove()
                            }
                        }
                    } else {
                        listenerEmployerResponse.remove()
                    }
                } else {
                    listenerEmployerResponse.remove()
                }
            }
    }

    private fun addMatchHistory(
        user: User,
        userUidToHistory: String,
        typeEmployeeRoleEnum: TypeEmployeeRoleEnum
    ) {
        val history = hashMapOf(
            "userUid" to userUidToHistory,
            "roleEmployer" to typeEmployeeRoleEnum.role,
            "status" to EmployerStatusEnum.AVAILABLE.status
        )

        if (user.typeUserEnum == TypeUserEnum.BUSINESS) {
            firestore.collection(businessCollection).document(user.uid!!)
                .collection(historyCollection).document().set(history)
        } else {
            firestore.collection(employersCollection).document(user.uid!!)
                .collection(historyCollection).document().set(history)
        }
    }

    override suspend fun getContracts(user: User) {
        val listContracts = mutableListOf<Contract>()

        val collection =
            if (user.typeUserEnum == TypeUserEnum.BUSINESS) businessCollection else employersCollection

        contractsListener = firestore.collection(collection)
            .document(user.uid!!)
            .collection(historyCollection).addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    listContracts.clear()

                    for (responseContract in snapshot.documents) {
                        val userUid = responseContract.getString("userUid")
                        val status = responseContract.getString("status")
                        val role = responseContract.getString("roleEmployer")

                        if (userUid != null && status != null && role != null) {
                            listContracts.add(
                                Contract(
                                    userUid,
                                    Util.getEmployerRole(role),
                                    Util.getEmployerStatus(status),
                                    responseContract.id
                                )
                            )
                        }
                    }

                    Log.d(Util.TAG, "Update contracts data $listContracts")
                    isNewContractsData = true
                    _dataContracts.value = Resource.Successful(listContracts)
                }
            }
    }

    override suspend fun updateStatusBusiness(currentUser: User): Resource<Boolean> {
        return try {
            if (currentUser.uid != null) {
                Log.d(Util.TAG, "Starting the change")

                val response =
                    firestore.collection(businessCollection).document(currentUser.uid!!).get()
                        .await()

                val isOpen = response.getBoolean("isOpenTheBusiness") ?: false
                Log.d(Util.TAG, "Current value in remote is $isOpen")

                Log.d(Util.TAG, "Current value to change is ${!isOpen}")

                firestore.collection(businessCollection)
                    .document(currentUser.uid!!)
                    .update("isOpenTheBusiness", !isOpen)

                Log.d(Util.TAG, "Finishing the change")

                Resource.Successful(!isOpen)
            } else {
                Resource.Error("Does not have UID")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateStatusOrder(businessUid: String, order: Order): Resource<Boolean> {
        return try {
            Log.d(Util.TAG, "Starting the status change for the order")

            firestore.collection(businessCollection)
                .document(businessUid).collection(ordersCollection)
                .document(order.documentReference!!)
                .update("status", order.status.status)

            Log.d(Util.TAG, "Finishing the change for the order")

            Resource.Successful(true)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun addOrder(businessUid: String, order: Order): Resource<Order> {
        return try {
            run {
                // TO DO - Create like a payOrder or closeOrder and add a payMethod

                val orderData = hashMapOf(
                    "idTable" to order.idTable,
                    "idWaiter" to order.idWaiter,
                    "status" to order.status.status,
                    "date" to order.date.toString(),
                    "total" to order.total,
                    "idChef" to order.idChef
                )

                Log.d(Util.TAG, "Order data $orderData")

                val response = firestore.collection(businessCollection)
                    .document(businessUid)
                    .collection(ordersCollection)
                    .add(orderData).await()

                order.documentReference = response.id

                for (resource in order.resourcesAdditional) {
                    val resourceData = hashMapOf(
                        "documentReference" to resource.resourceBusiness.documentReference,
                        "amount" to resource.amount
                    )

                    firestore.collection(businessCollection)
                        .document(businessUid)
                        .collection(ordersCollection).document(order.documentReference!!)
                        .collection(itemsOrderCollection).add(resourceData).await()
                }

                for (menu in order.menus) {
                    val menuData = hashMapOf(
                        "documentReference" to menu.menu.documentReference,
                        "amount" to menu.amount.toInt()
                    )

                    firestore.collection(businessCollection)
                        .document(businessUid)
                        .collection(ordersCollection).document(order.documentReference!!)
                        .collection(menusOrderCollection).add(menuData).await()
                }

                Log.d(Util.TAG, "Order sent to firestore")


                closeTable(businessUid, order.idTable)
                Resource.Successful(order)
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    private suspend fun closeTable(businessUid: String, idTable: String) {
        firestore.collection(businessCollection)
            .document(businessUid)
            .collection(tablesCollection).document(idTable)
            .update("status", TableStatusEnum.NOT_AVAILABLE.status).await()
    }

    override suspend fun getOrders(businessUid: String) {
        ordersListener = firestore.collection(businessCollection)
            .document(businessUid)
            .collection(ordersCollection)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val listOrders =
                            snapshot.documents.mapNotNull { parseOrder(it, businessUid) }

                        Log.d(Util.TAG, "LIST ORDER DATA: $listOrders")

                        withContext(Dispatchers.Main) {
                            isNewOrdersData = true
                            _dataOrders.value = Resource.Successful(listOrders)
                        }
                    }
                }
            }
    }

    private suspend fun parseOrder(document: DocumentSnapshot, businessUid: String): Order? {
        val date = document.getString("date") ?: return null
        val idTable = document.getString("idTable") ?: return null
        val idChef = document.getString("idChef") ?: ""
        val idWaiter = document.getString("idWaiter") ?: return null
        val status = Util.getOrderStatusEnum(document.getString("status") ?: "")
        val total = document.getLong("total") ?: return null

        val resources = getResourcesForOrder(document, businessUid)
        val menus = getMenusForOrder(document, businessUid)

        return Order(
            resourcesAdditional = resources,
            menus = menus,
            idTable = idTable,
            idWaiter = idWaiter,
            status = status,
            date = LocalDate.parse(date),
            total = total.toFloat(),
            comments = "",
            idChef = idChef,
            documentReference = document.id
        ).also { Log.d(Util.TAG, "Order to get: $it") }
    }

    private suspend fun getResourcesForOrder(
        orderDocument: DocumentSnapshot,
        businessUid: String
    ): List<ResourceWithAmountInMenu> {
        val resourceSnapshots =
            orderDocument.reference.collection(itemsOrderCollection).get().await()
        return resourceSnapshots.documents.mapNotNull { parseResource(it, businessUid) }
    }

    private suspend fun parseResource(
        document: DocumentSnapshot,
        businessUid: String
    ): ResourceWithAmountInMenu? {
        val documentReference = document.getString("documentReference") ?: return null
        val amount = document.getLong("amount")?.toFloat() ?: return null

        val resourceSnapshot = firestore.collection(businessCollection)
            .document(businessUid)
            .collection(resourcesBusinessCollection)
            .document(documentReference)
            .get()
            .await()

        return ResourceBusiness(
            name = resourceSnapshot.getString("name") ?: return null,
            minStock = resourceSnapshot.getLong("minimumStock")?.toShort() ?: return null,
            maxStock = resourceSnapshot.getLong("maximumStock")?.toShort() ?: return null,
            price = resourceSnapshot.getDouble("price")?.toFloat() ?: return null,
            amount = resourceSnapshot.getDouble("amount")?.toFloat() ?: return null,
            typeResourceEnum = Util.getTypeResource(
                resourceSnapshot.getString("typeResource") ?: ""
            ),
            typeMeasurementEnum = Util.getTypeMeasurement(
                resourceSnapshot.getString("typeMeasurement") ?: ""
            ),
            documentReference = resourceSnapshot.id
        ).let { ResourceWithAmountInMenu(it, amount) }
    }

    private suspend fun getMenusForOrder(
        orderDocument: DocumentSnapshot,
        businessUid: String
    ): List<MenusWithAmountInOrder> {
        val menuSnapshots = orderDocument.reference.collection(menusOrderCollection).get().await()
        return menuSnapshots.documents.mapNotNull { parseMenu(it, businessUid) }
    }

    private suspend fun parseMenu(
        document: DocumentSnapshot,
        businessUid: String
    ): MenusWithAmountInOrder? {
        val amountForMenuInOrder = document.getLong("amount")?.toShort() ?: return null
        val documentReference = document.getString("documentReference") ?: return null

        val menuSnapshot = firestore.collection(businessCollection)
            .document(businessUid)
            .collection(menusCollection)
            .document(documentReference)
            .get()
            .await()

        val name = menuSnapshot.getString("name") ?: return null
        val price = menuSnapshot.getDouble("price")?.toFloat() ?: return null

        val resources = getResourcesForMenu(menuSnapshot, businessUid)

        return MenusWithAmountInOrder(
            menu = Menu(name = name, listResourceBusiness = resources, price = price),
            amount = amountForMenuInOrder
        )
    }

    private suspend fun getResourcesForMenu(
        menuDocument: DocumentSnapshot,
        businessUid: String
    ): List<ResourceWithAmountInMenu> {
        val resourceSnapshots =
            menuDocument.reference.collection(componentsCollection).get().await()
        return resourceSnapshots.documents.mapNotNull { parseResource(it, businessUid) }
    }

    /*private fun searchUser(business: Business?): DocumentReference {
        return firestore.collection("Users").document(business?.id ?: "")
    }

    override suspend fun createGroup(group: Group): Resource<String> {
        return try {
            Log.d("AndroidRuntime", "Start again xd")

            val reference = searchUser(group.author ?: null)

            val groupToCreate = hashMapOf(
                "name" to group.name,
                "date" to group.date.toString(),
                "description" to group.description,
                "author" to reference
            )

            firestore.collection("Groups").add(groupToCreate).await()

            Resource.Successful()
        } catch (e: Exception) {
            Log.d("AndroidRuntime", "Error")
            Resource.Error(e.message.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getGroups(business: Business): Resource<List<Group>> {
        return try {
            val reference = searchUser(business)
            val groups: MutableList<Group> = mutableListOf()

            val listGroups = firestore.collection("Groups").get().await()

            for (i in listGroups) {
                if (i.data.containsValue(reference)) {
                    val group = Group(
                        name = i.data["name"].toString(),
                        date = LocalDateTime.parse(i.data["date"].toString()),
                        description = i.data["description"].toString()
                    )
                    groups.add(group)
                }
            }

            Resource.Successful(groups)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }*/
}
