package com.xsofty.shared.nav

object DeepLinkHandler {
    private const val PREFIX = "komarista://"
    const val SIGN_IN = "${PREFIX}signIn"
    const val CATEGORIES = "${PREFIX}categories"
    const val ROOMS = "${PREFIX}rooms"
    const val MY_ROOMS = "${PREFIX}myRooms"
    const val ROOM_DETAILS = "${PREFIX}roomDetails"
    const val CREATE_ROOM = "${PREFIX}createRoom"
}
