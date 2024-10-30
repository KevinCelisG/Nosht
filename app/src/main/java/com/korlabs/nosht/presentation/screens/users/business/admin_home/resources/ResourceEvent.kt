package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import com.korlabs.nosht.domain.model.ResourceBusiness
import com.korlabs.nosht.domain.model.ResourceMovement

sealed class ResourceEvent {
    data class Create(val resourceBusiness: ResourceBusiness) : ResourceEvent()
    data class Delete(val resourceBusiness: ResourceBusiness) : ResourceEvent()
    data class Add(val resourceBusiness: ResourceBusiness, val resourceMovement: ResourceMovement) : ResourceEvent()
    data class Update(val resourceBusiness: ResourceBusiness) : ResourceEvent()
    data object GetRemoteResourceBusiness : ResourceEvent()
    data object GetLocalResourceBusiness : ResourceEvent()
}
