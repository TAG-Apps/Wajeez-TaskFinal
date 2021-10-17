package  com.wajeez.sample.model.data

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.firestore.auth.User
import com.squareup.moshi.*
import java.io.IOException
import javax.annotation.Nullable


/*@JsonClass(generateAdapter = true)
data class UserAdapter(
    val id: String?,
    val name: String?,
    val profilePictureUrl: String?
    )*/

class UserAdapter {

   @ToJson
   fun toJson(userModel: UserModel): String? {
       return userModel.id + userModel.name + userModel.profilePictureUrl
   }

   @FromJson
   fun fromJson(user: String): UserModel? {
       if (user.length < 0) throw JsonDataException("Unknown card: $user")
       return UserModel("1", "ihab", "")

   }
}

  /* @FromJson
   fun usersFromJson(userModel: UserModel): UserModel? {
       val user = UserModel()
       user.id = userModel.id
       user.name= userModel.name
       user.profilePictureUrl = user.profilePictureUrl
       return user
   }*/

   /*@ToJson
   fun toJson(users: UserModel): UserModel {
       return
   }

   @FromJson
   fun fromJson(): UserModel {

   }/*


/*: JsonAdapter<UserModel>() {


   override fun fromJson(reader: JsonReader): UserModel? {
       TODO("Not yet implemented")

   }

   override fun toJson(writer: JsonWriter, value: UserModel?) {
       TODO("Not yet implemented")
   }*/