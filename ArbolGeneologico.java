package practica2;

import java.util.ArrayList;
import java.util.Scanner;

class Persona {
    private String nombre;
    private Persona padre;
    private Persona madre;
    ArrayList<Persona> hijos;
    
        public Persona(String nombre) {
            this.nombre = nombre;
            this.hijos = new ArrayList<>();
        }
    
        public void setPadre(Persona padre) {
            this.padre = padre;
        }
    
        public void setMadre(Persona madre) {
            this.madre = madre;
        }
    
        public void agregarHijo(Persona hijo) {
            this.hijos.add(hijo);
        }
    
        public String getNombre() {
            return nombre;
        }
    
        public void mostrarArbol(int nivel) {
            for (int i = 0; i < nivel; i++) {
                System.out.print("  "); 
            }
            System.out.println(nombre);
    
            for (Persona hijo : hijos) {
                hijo.mostrarArbol(nivel + 1); 
            }
        }
    }
    
    public class ArbolGeneologico {
        private static Scanner scanner = new Scanner(System.in);
    
        public static void main(String[] args) {
            Persona raiz = null; 
    
            int opcion;
            do {
                System.out.println("\nMenú:");
                System.out.println("1. Agregar miembro a la familia");
                System.out.println("2. Mostrar árbol genealógico");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); 
    
                switch (opcion) {
                    case 1:
                        raiz = agregarMiembro(raiz); 
                        break;
                    case 2:
                        if (raiz != null) {
                            System.out.println("\nÁrbol Genealógico:");
                            raiz.mostrarArbol(0); 
                        } else {
                            System.out.println("Primero debes agregar a un miembro a la familia.");
                        }
                        break;
                    case 3:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (opcion != 3);
        }
    
        private static Persona agregarMiembro(Persona raiz) {
            System.out.print("Ingrese el nombre del miembro: ");
            String nombre = scanner.nextLine();
            Persona nuevaPersona = new Persona(nombre);
    
            if (raiz != null) {
                System.out.print("¿Es este miembro un hijo de alguien? (si/no): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                if (respuesta.equals("si")) {
                    System.out.print("¿Quién es el padre de " + nombre + "? ");
                    String nombrePadre = scanner.nextLine();
                    Persona padre = buscarPersona(raiz, nombrePadre);
                    if (padre != null) {
                        nuevaPersona.setPadre(padre);
                        padre.agregarHijo(nuevaPersona);
                    }
    
                    System.out.print("¿Quién es la madre de " + nombre + "? ");
                    String nombreMadre = scanner.nextLine();
                    Persona madre = buscarPersona(raiz, nombreMadre);
                    if (madre != null) {
                        nuevaPersona.setMadre(madre);
                        madre.agregarHijo(nuevaPersona);
                    }
                }
            }
            if (raiz == null) {
                raiz = nuevaPersona;
            }
            return raiz;
        }
        private static Persona buscarPersona(Persona raiz, String nombre) {
            if (raiz == null) return null;
            if (raiz.getNombre().equalsIgnoreCase(nombre)) return raiz;
            for (Persona hijo : raiz.hijos) {
            Persona personaEncontrada = buscarPersona(hijo, nombre);
            if (personaEncontrada != null) return personaEncontrada;
        }
        return null;
    }
}
