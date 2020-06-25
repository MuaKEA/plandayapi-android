package dk.nodes.template.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateEmployee (

        @SerializedName("firstName")
        @Expose
        var firstName: String? = null,
        @SerializedName("lastName")
        @Expose
        var lastName: String? = null,
        @SerializedName("gender")
        @Expose
        var gender: String? = null

)