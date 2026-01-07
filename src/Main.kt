import java.util.Scanner

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
    val scanner = Scanner(System.`in`)
    val tasks = mutableListOf<Task>()
    var nextId = 1

    while (true) {
        printMenu()
        when (scanner.nextLine().trim()) {
            "1" -> nextId = addTask(scanner, tasks, nextId)
            "2" -> listTasks(tasks)
            "3" -> updateTask(scanner, tasks)
            "4" -> deleteTask(scanner, tasks)
            "5" -> filterTasks(scanner, tasks)
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

fun addTask(scanner: Scanner, tasks: MutableList<Task>, nextId: Int): Int {
    print("Title: ")
    val title = scanner.nextLine().trim()
    if (title.isEmpty()) {
        println("Title cannot be empty")
        return nextId
    }

    print("Description: ")
    val description = scanner.nextLine().trim()

    print("Priority (LOW / MEDIUM / HIGH): ")
    val priority = try {
        Priority.valueOf(scanner.nextLine().trim().uppercase())
    } catch (e: Exception) {
        println("Invalid priority")
        return nextId
    }

    val task = Task(nextId, title, description, priority, Status.TODO)
    tasks.add(task)

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

fun updateTask(scanner: Scanner, tasks: MutableList<Task>) {
    print("Enter task ID: ")
    val id = scanner.nextLine().toIntOrNull()

    val task = tasks.find { it.id == id }
    task?.let {
        print("New status (TODO / IN_PROGRESS / DONE): ")
        val newStatus = try {
            Status.valueOf(scanner.nextLine().trim().uppercase())
        } catch (e: Exception) {
            println("Invalid status")
            return
        }
        it.status = newStatus
        println("Task updated")
    } ?: println("Task not found")
}

fun deleteTask(scanner: Scanner, tasks: MutableList<Task>) {
    print("Enter task ID to delete: ")
    val id = scanner.nextLine().toIntOrNull()

    val removed = tasks.removeIf { it.id == id }
    println(if (removed) "Task deleted" else "Task not found")
}

fun filterTasks(scanner: Scanner, tasks: List<Task>) {
    println("1. Filter by status")
    println("2. Sort by priority")
    when (scanner.nextLine().trim()) {
        "1" -> {
            print("Status (TODO / IN_PROGRESS / DONE): ")
            val status = try {
                Status.valueOf(scanner.nextLine().trim().uppercase())
            } catch (e: Exception) {
                println("Invalid status")
                return
            }

            val result = tasks.filter { it.status == status }
            result.forEach { println(it) }
        }

        "2" -> {
            val sorted = tasks.sortedBy { it.priority }
            sorted.forEach { println(it) }
        }

        else -> println("Invalid option")
    }
}
