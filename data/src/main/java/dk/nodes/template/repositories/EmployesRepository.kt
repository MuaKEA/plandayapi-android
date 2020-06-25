package dk.nodes.template.repositories

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import dk.nodes.template.models.*
import dk.nodes.template.network.IEmployesService
import dk.nodes.template.presentation.BuildConfig
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.ArrayList
import javax.inject.Inject

class EmployesRepository @Inject constructor(private val iEmployesService: IEmployesService,
                                             private val sharedPreferences: SharedPreferences) {


    fun getHeader(): HashMap<String, String> {
        val mapHeader = HashMap<String, String>()
        val authToken = sharedPreferences.getString("access_token", null)
        if (authToken != null) {
            mapHeader.put("X-ClientId", BuildConfig.client_id)
            mapHeader.put("Authorization", "Bearer $authToken")
        }

        return mapHeader
    }


    suspend fun fetchemployees(): ArrayList<Employee> {

        val employyeList = ArrayList<Employee>()
        delay(2000)

        val response = iEmployesService.fetchEmployees(getHeader()).execute()

        if (response.isSuccessful) {

            val message = response.body()

            if (message != null) {

                employyeList.addAll(message.employeeList)

            }

        }

        return employyeList


    }





    suspend fun fetchFromId(id: Int): Employee? {

        val response = iEmployesService.fetchEmployeesById(getHeader(), id).execute()

        if (response.isSuccessful) {
            var message = response.body()
            if (message != null) {
                return message
            }
        }
        return null
    }

    suspend fun fetchDepartementNames(): ArrayList<DepartmentsInfo> {
        delay(2000)

        val response = iEmployesService.fetchDepartmentNames(getHeader()).execute()
        val departmentList = ArrayList<DepartmentsInfo>()

        if (response.isSuccessful) {
            val message = response.body()

            if (message != null) {

                message.departmentsList?.let { departmentList.addAll(it) }

            }
        }

        return departmentList
    }

    suspend fun sendEmployee(employee: EditedEmployee): Unit? {

        val headers = getHeader()

        headers.put(" Content-type", "application/json")

        val response = iEmployesService.updateEmployee(employee.id, UpdateEmployee(employee.firstName, employee.lastName, employee.gender), BuildConfig.client_id, getHeader().get("Authorization").toString()).execute()

        if (response.isSuccessful) {

            return null

        } else {

            throw Exception()
        }

    }
}







