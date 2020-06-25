package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import dk.nodes.template.models.DepartmentsInfo
import dk.nodes.template.models.Employee
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {
    private val viewModel by viewModel<MainActivityViewModel>()
    private var adapter: EmployeesAdapter? = null
    private var employeesList: ArrayList<Employee>? = null
    lateinit var editemployeeItent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.fetchAuth()
        viewModel.fetchEmployes()
        viewModel.fetchDepartments()

        loadingbar.isEnabled= true


        adapter = EmployeesAdapter(this, R.layout.employees_row)
        adapter?.onItemClickedListener = { employee ->

            editemployeeItent = Intent(this, EditEmployeeActivity::class.java)
            editemployeeItent.putExtra("empId", employee.id)
            startActivity(editemployeeItent)
        }

        viewModel.viewState.observeNonNull(this) { state ->
            fetchemployee(state)
            fetchdepartments(state)
            handleError(state)
        }


    }

    private fun handleError(state: MainActivityViewState) {
        state.viewError?.let { error->
        Toast.makeText(this,"Error something went wrong" , Toast.LENGTH_LONG).show()
        }

    }

    private fun fetchdepartments(viewState: MainActivityViewState) {
        viewState.departmentsList?.let { departmentsList ->
            excangeDpt(departmentsList)
        }
    }

    private fun excangeDpt(dptList: ArrayList<DepartmentsInfo>) {

        employeesList?.let {

            for (empl in it) {

                if (empl.departments != null) {
                    empl.departments!!.forEachIndexed { index, element ->

                        for (dpt in dptList) {

                            if (dpt.id.toString() == element) {

                                empl.departments!![index] = dpt.name.toString()
                            }
                        }
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchEmployes()
        viewModel.fetchDepartments()


    }

    private fun fetchemployee(viewState: MainActivityViewState) {
        viewState.employyeList?.let { employeedata ->
            loadingbar.visibility = View.GONE;
            employeesList = employeedata
            adapter?.addemployeesList(employeedata)
            Timber.e(employeedata.size.toString())
            updateRecyclerview()
        }
    }

    private fun updateRecyclerview() {
        rv_employeesdetails.layoutManager = LinearLayoutManager(this)
        rv_employeesdetails.adapter = adapter
        adapter?.notifyDataSetChanged()

    }


}