package  com.wajeez.sample.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel (
    @Json(name = "id") var id: String?,
    @Json(name = "name") var name: String?,
    @Json(name = "profilePictureUrl") var profilePictureUrl: String?
    )

{
    constructor() : this("", "",
        ""
    )
}
