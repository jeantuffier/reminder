package no.hyper.reminder.display.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_task_activity.*
import no.hyper.reminder.R
import no.hyper.reminder.common.Reminder
import no.hyper.reminder.common.extension.getInteger
import no.hyper.reminder.common.extension.toDp
import no.hyper.reminder.display.injection.DisplayTaskActivityModule
import no.hyper.reminder.common.recycler.SpaceItemDecoration
import no.hyper.reminder.create.view.activity.CreateTaskActivity
import no.hyper.reminder.display.presenter.ProvidedDisplayTaskPresenterOps
import javax.inject.Inject

class DisplayTaskActivity : AppCompatActivity(), RequiredDisplayTaskViewOps {

    companion object {
        val TASK_LIST_POSITION = "DisplayTaskActivity.TASK_LIST_POSITION"
    }

    @Inject
    lateinit var presenter : ProvidedDisplayTaskPresenterOps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_task_activity)

        setComponent()
        setToolbar()
        setRecyclerView()

        task_create_button.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivityForResult(intent, getInteger(R.integer.request_create_task))
        }

        presenter.createDatabase()
        presenter.loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == getInteger(R.integer.request_create_task) &&
                resultCode == getInteger(R.integer.result_create_task_success)) {
            updateRecycler()
        }
    }

    override fun getActivityContext() = this

    override fun notifyItemInserted() { }

    private fun setComponent() {
        Reminder.get(this).component
                .getTaskListComponent(DisplayTaskActivityModule(this))
                .inject(this)
    }

    private fun setToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setRecyclerView() {
        val layout = LinearLayoutManager(this)
        task_recycler.layoutManager = layout
        task_recycler.addItemDecoration(SpaceItemDecoration(16.toDp(this)))
        task_recycler.adapter = TaskAdapter()
    }

    private fun updateRecycler() {
        presenter.loadData()
        val position = presenter.getTasksCount()
        task_recycler.adapter.notifyItemInserted(position)
    }

    private inner class TaskAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount() = presenter.getTasksCount()

        override fun getItemViewType(position: Int) = presenter.getViewType(position)

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return presenter.createViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            presenter.bindViewHolder(holder, position)
        }

    }

}
