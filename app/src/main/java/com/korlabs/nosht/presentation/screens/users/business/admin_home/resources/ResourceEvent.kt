package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import com.korlabs.nosht.domain.model.ResourceBusiness

sealed class ResourceEvent {
    data class Add(val resourceBusiness: ResourceBusiness) : ResourceEvent()
    data object GetRemoteResourceBusiness : ResourceEvent()
    data object GetLocalResourceBusiness : ResourceEvent()
}
