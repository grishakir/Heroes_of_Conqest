package com.thekingames.medivalwarriors2.core.model.tasks

import com.thekingames.medivalwarriors2.core.R
import com.thekingames.medivalwarriors2.core.model.shop.Item
import com.thekingames.medivalwarriors2.core.model.shop.ToolsItem

abstract class Task(var idIcon: Int, var idName: Int, var idDescription: Int, var idMessage1: Int, var idMessage2: Int, var chance: Int = 50, var items: Array<Item> = arrayOf())

class Task1 : Task(R.drawable.task_01, R.string.task_name_01, R.string.task_desc_01, R.string.task_p_01, R.string.task_n_01, 40, arrayOf(ToolsItem()))
class Task2 : Task(R.drawable.task_02, R.string.task_name_02, R.string.task_desc_02, R.string.task_p_02, R.string.task_n_02, 70)
class Task3 : Task(R.drawable.task_03, R.string.task_name_03, R.string.task_desc_03, R.string.task_p_03, R.string.task_n_03)
class Task4 : Task(R.drawable.task_04, R.string.task_name_04, R.string.task_desc_04, R.string.task_p_04, R.string.task_n_04, 60)
class Task5 : Task(R.drawable.task_05, R.string.task_name_05, R.string.task_desc_05, R.string.task_p_05, R.string.task_n_05)
class Task6 : Task(R.drawable.task_06, R.string.task_name_06, R.string.task_desc_06, R.string.task_p_06, R.string.task_n_06, 5)
class Task7 : Task(R.drawable.task_07, R.string.task_name_07, R.string.task_desc_07, R.string.task_p_07, R.string.task_n_07)
class Task8 : Task(R.drawable.task_08, R.string.task_name_08, R.string.task_desc_08, R.string.task_p_08, R.string.task_n_08, 30)
class Task9 : Task(R.drawable.task_09, R.string.task_name_09, R.string.task_desc_09, R.string.task_p_09, R.string.task_n_09, 20)
class Task10 : Task(R.drawable.task_10, R.string.task_name_10, R.string.task_desc_10, R.string.task_p_10, R.string.task_n_10, 10)