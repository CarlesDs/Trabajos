package practica4;

import java.io.*;
import java.util.Scanner;

class Task implements Serializable {
    private String name;
    private String descr;
    private boolean isUrgent;
    private boolean isCompleted;
    private Task next;

    public Task(String name, String descr, boolean isUrgent) {
        this.name = name;
        this.descr = descr;
        this.isUrgent = isUrgent;
        this.isCompleted = false;
        this.next = null;
    }

    public String getName() { return name; }
    public String getDescr() { return descr; }
    public void addDescr(String extraDescr) { this.descr += " | " + extraDescr; }
    public boolean isUrgent() { return isUrgent; }
    public boolean isCompleted() { return isCompleted; }
    public void markCompleted() { this.isCompleted = true; }

    public Task getNext() { return next; }
    public void setNext(Task next) { this.next = next; }
}

class TaskList implements Serializable {
    private Task head;

    public void add(String name, String descr, boolean isUrgent) {
        Task newTask = new Task(name, descr, isUrgent);
        if (head == null || (isUrgent && !head.isUrgent())) {
            newTask.setNext(head);
            head = newTask;
            return;
        }
        
        Task current = head;
        Task prev = null;
        while (current != null && (current.isUrgent() || !isUrgent)) {
            prev = current;
            current = current.getNext();
        }
        prev.setNext(newTask);
        newTask.setNext(current);
    }

    public void showTasks() {
        if (head == null) {
            System.out.println("No hay tareas.");
            return;
        }

        Task tmp = head;
        int index = 1;
        System.out.println("\nLista de Tareas:");
        while (tmp != null) {
            String estado = tmp.isCompleted() ? "[COMPLETADA] " : "";
            String prioridad = tmp.isUrgent() ? "[URGENTE] " : "[NORMAL] ";
            System.out.println(index + ". " + estado + prioridad + tmp.getName() + ": " + tmp.getDescr());
            tmp = tmp.getNext();
            index++;
        }
    }

    public Task getTaskAt(int index) {
        Task tmp = head;
        int count = 1;
        while (tmp != null) {
            if (count == index) return tmp;
            tmp = tmp.getNext();
            count++;
        }
        return null;
    }

    public void deleteTask(int index) {
        if (head == null) return;
        if (index == 1) {
            head = head.getNext();
            return;
        }
        Task prev = null;
        Task current = head;
        int count = 1;
        while (current != null && count != index) {
            prev = current;
            current = current.getNext();
            count++;
        }
        if (current != null) prev.setNext(current.getNext());
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error al guardar las tareas.");
        }
    }

    public static TaskList loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (TaskList) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new TaskList();
        }
    }
}

public class GestionTareasinversa {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskList lista = TaskList.loadFromFile("tareas.dat");

        int opcion;
        do {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Mostrar tareas");
            System.out.println("3. Modificar descripción");
            System.out.println("4. Eliminar tarea");
            System.out.println("5. Marcar como completada");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Nombre de la tarea: ");
                    String name = scanner.nextLine();
                    System.out.print("Descripción: ");
                    String descr = scanner.nextLine();
                    System.out.print("¿Es urgente? (si/no): ");
                    boolean isUrgent = scanner.nextLine().trim().equalsIgnoreCase("si");
                    lista.add(name, descr, isUrgent);
                    System.out.println("Tarea agregada.");
                    break;
                case 2:
                    lista.showTasks();
                    break;
                case 3:
                    lista.showTasks();
                    System.out.print("Número de la tarea a modificar: ");
                    Task selectedTask = lista.getTaskAt(scanner.nextInt());
                    scanner.nextLine();
                    if (selectedTask != null) {
                        System.out.print("Nueva descripción: ");
                        selectedTask.addDescr(scanner.nextLine());
                        System.out.println("Descripción actualizada.");
                    } else {
                        System.out.println("Número inválido.");
                    }
                    break;
                case 4:
                    lista.showTasks();
                    System.out.print("Número de la tarea a eliminar: ");
                    lista.deleteTask(scanner.nextInt());
                    System.out.println("Tarea eliminada.");
                    break;
                case 5:
                    lista.showTasks();
                    System.out.print("Número de la tarea a marcar como completada: ");
                    Task taskToComplete = lista.getTaskAt(scanner.nextInt());
                    if (taskToComplete != null) {
                        taskToComplete.markCompleted();
                        System.out.println("Tarea marcada como completada.");
                    } else {
                        System.out.println("Número inválido.");
                    }
                    break;
                case 6:
                    lista.saveToFile("tareas.dat");
                    System.out.println("Tareas guardadas. Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 6);

        scanner.close();
    }
}
