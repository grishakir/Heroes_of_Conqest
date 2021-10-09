package com.thekingames.medivalwarriors2.core.view.screens

import android.view.View
import android.view.ViewGroup
import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.tasks.*
import com.thekingames.medivalwarriors2.core.view.activity.MainActivity
import com.thekingames.screenmanager.Fragment
import com.thekingames.screenmanager.Screen
import com.thekingames.tasks.TaskView
import kotlinx.android.synthetic.main.a2_tasks.view.*
import kotlinx.android.synthetic.main.c_task.view.*
import java.util.*

class TasksScreen(parent: ViewGroup) : Screen(parent, R.layout.a2_tasks) {
    private val a: MainActivity = activity as MainActivity

    private val tasks: Array<Task> = arrayOf(
            Task1(),
            Task2(),
            Task3(),
            Task4(),
            Task5(),
            Task6(),
            Task7(),
            Task8(),
            Task9(),
            Task10())

    private var tasksView = arrayListOf<TaskCard>()

    override fun releaseDate() {
        super.releaseDate()
        val r = Random(System.currentTimeMillis())
        view.tasks_container.removeAllViews()
        tasksView = arrayListOf()
        tasks.forEach {
            tasksView.add(TaskCard(view.tasks_container, it, TaskView(activity, it.idIcon)))
        }
        tasksView.forEachIndexed { i, it ->
            if (a.player.tasksEnable[i] && i < tasks.size && i < a.player.heroes.maxBy { it.lvl }!!.lvl) {
                it.setTaskExecuteListener(View.OnClickListener {
                    a.player.tasksEnable[i] = false
                    tasksView[i].hide()
                    if (r.nextInt(101) <= tasks[i].chance) {
                        a.messageDialog.create(a.parent.parent as ViewGroup)
                        a.messageDialog.result = true
                        a.messageDialog.coins = 100
                        a.messageDialog.crystalls = 1
                        val exp = (a.player.getCurrentHero().capExp * 0.05).toInt()
                        a.messageDialog.exp = exp
                        a.player.getCurrentHero().exp += exp
                        a.player.coins += 100
                        a.player.crystals += 1
                        a.player.inventory.addAll(tasks[i].items)
                        a.messageDialog.textDescription = tasks[i].idMessage1
                    } else {
                        a.messageDialog.create(a.parent.parent as ViewGroup)
                        a.messageDialog.result = false
                        a.messageDialog.coins = 0
                        a.messageDialog.crystalls = 0
                        a.messageDialog.exp = 0
                        a.messageDialog.textDescription = tasks[i].idMessage2
                    }
                })
                it.show()
            }
        }
        (activity as MainActivity).setHeader(R.string.b_tasks)
    }
}

class TaskCard(parent: ViewGroup, var task: Task, var taskView: TaskView) : Fragment(parent, R.layout.c_task) {

    fun setTaskExecuteListener(onClickListener: View.OnClickListener) {
        view.execute.setOnClickListener(onClickListener)
    }

    fun hide() {
        parent.removeView(view)
    }

    override fun releaseDate() {
        super.releaseDate()
        view.task_name.setText(task.idName)
        view.task_desc.setText(task.idDescription)
        view.task_view_container.addView(taskView.view)
    }
}