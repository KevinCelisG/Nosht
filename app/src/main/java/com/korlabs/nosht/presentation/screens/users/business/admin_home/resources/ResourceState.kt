package com.korlabs.nosht.presentation.screens.users.business.admin_home.resources

import com.korlabs.nosht.domain.model.ResourceBusiness

data class ResourceState(
    val isLoading: Boolean = false,
    val resourceBusiness: ResourceBusiness? = null,
    val listResourceBusiness: List<ResourceBusiness> = emptyList(),
    val isNewRemoteResources: Boolean = false
)
