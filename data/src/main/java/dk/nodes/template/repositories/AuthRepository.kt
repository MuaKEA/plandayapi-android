package dk.nodes.template.repositories


import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonObject
import dk.nodes.template.models.Authentication
import dk.nodes.template.network.IAutService
import dk.nodes.template.presentation.BuildConfig
import dk.nodes.template.presentation.BuildConfig.VERSION_NAME
import javax.inject.Inject

class AuthRepository @Inject constructor(private val iAutService: IAutService,  private val sharedPreferences: SharedPreferences) {


    suspend fun fetch_Token(): String? {

        val response = iAutService.getTestMessage(BuildConfig.client_id, "refresh_token", BuildConfig.refresh_token).execute()

        if (response.isSuccessful) {

            val message = response.body()

            if (message != null) {

                message.message?.let {
                    sharedPreferences.edit().putString("access_token", it).apply()

                    return it

                }
            }
        }

        return null
    }




}