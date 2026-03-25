package com.germandebustamante.ringtonemanager.core.model.authorization

object UserBOMother {

    fun default() = UserBO(
        id = "user_1",
        email = "test@example.com",
    )

    fun random() = UserBO(
        id = "user_${(1..100).random()}",
        email = "user${(1..100).random()}@example.com",
    )
}
