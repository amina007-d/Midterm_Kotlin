interface Identifiable {
    val id: Int
}

enum class Priority { LOW, MEDIUM, HIGH }
enum class Status { TODO, IN_PROGRESS, DONE }

data class Task(
    override val id: Int,
    var title: String,
    var description: String,
    var priority: Priority,
    var status: Status
) : Identifiable

fun main() {
    val tasks = mutableListOf<Task>()
    var nextId = 1

    while (true) {
        printMenu()
        when (readLine()?.trim()) {
            "1" -> nextId = addTask(tasks, nextId)
            "2" -> listTasks(tasks)
            "3" -> updateTask(tasks)
            "4" -> deleteTask(tasks)
            "5" -> filterTasks(tasks)
            "6" -> return
            else -> println("Invalid option")
        }
    }
}

fun printMenu() {
    println(
        """
        |Task Tracker
        |1. Add task
        |2. List tasks
        |3. Update task
        |4. Delete task
        |5. Filter tasks
        |6. Exit
        """.trimMargin()
    )
}

fun addTask(tasks: MutableList<Task>, nextId: Int): Int {
    print("Title: ")
    val title = readLine()?.trim().orEmpty()
    if (title.isEmpty()) {
        println("Title cannot be empty")
        return nextId
    }

    print("Description: ")
    val description = readLine()?.trim().orEmpty()

    print("Priority (LOW / MEDIUM / HIGH): ")
    val priority = try {
        Priority.valueOf(readLine()?.trim()?.uppercase().orEmpty())
    } catch (e: Exception) {
        println("Invalid priority")
        return nextId
    }

    tasks.add(Task(nextId, title, description, priority, Status.TODO))
    println("Task added with ID $nextId")
    return nextId + 1
}

fun listTasks(tasks: List<Task>) {
    if (tasks.isEmpty()) {
        println("No tasks available")
        return
    }
    tasks.forEach { println(it) }
}

fun updateTask(tasks: MutableList<Task>) {
    print("Enter task ID: ")
    val id = readLine()?.toIntOrNull()

    val task = tasks.find { it.id == id }
    task?.let {
        print("New status (TODO / IN_PROGRESS / DONE): ")
        val newStatus = try {
            Status.valueOf(readLine()?.trim()?.uppercase().orEmpty())
        } catch (e: Exception) {
            println("Invalid status")
            return
        }
        it.status = newStatus
        println("Task updated")
    } ?: println("Task not found")
}

fun deleteTask(tasks: MutableList<Task>) {
    print("Enter task ID to delete: ")
    val id = readLine()?.toIntOrNull()

    val removed = tasks.removeIf { it.id == id }
    println(if (removed) "Task deleted" else "Task not found")
}

fun filterTasks(tasks: List<Task>) {
    println("1. Filter by status")
    println("2. Sort by priority")
    when (readLine()?.trim()) {
        "1" -> {
            print("Status (TODO / IN_PROGRESS / DONE): ")
            val status = try {
                Status.valueOf(readLine()?.trim()?.uppercase().orEmpty())
            } catch (e: Exception) {
                println("Invalid status")
                return
            }
            tasks.filter { it.status == status }.forEach { println(it) }
        }

        "2" -> {
            tasks.sortedBy { it.priority }.forEach { println(it) }
        }

        else -> println("Invalid option")
    }
}
