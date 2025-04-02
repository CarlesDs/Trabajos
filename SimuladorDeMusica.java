import java.util.Scanner;

class CancionNodo {
    Cancion cancion;
    CancionNodo siguiente;
    CancionNodo anterior; 

    public CancionNodo(Cancion cancion) {
        this.cancion = cancion;
        this.siguiente = null;
        this.anterior = null;
    }
}

class Cancion {
    int id;
    String nombre;
    String artista;

    public Cancion(int id, String nombre, String artista) {
        this.id = id;
        this.nombre = nombre;
        this.artista = artista;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " de " + artista;
    }
}

class ListaDeCanciones {
    CancionNodo cabeza;
    CancionNodo cola;

    public ListaDeCanciones() {
        this.cabeza = null;
        this.cola = null;
    }

    public void agregarCancion(Cancion cancion) {
        CancionNodo nuevoNodo = new CancionNodo(cancion);
        if (cabeza == null) {
            cabeza = cola = nuevoNodo;
            cabeza.siguiente = cabeza; 
            cabeza.anterior = cabeza;
        } else {
            nuevoNodo.siguiente = cabeza;
            nuevoNodo.anterior = cola;
            cola.siguiente = nuevoNodo;
            cabeza.anterior = nuevoNodo;
            cola = nuevoNodo;
        }
    }

    public void eliminarCancion(int id) {
        if (cabeza == null) return;

        CancionNodo actual = cabeza;
        do {
            if (actual.cancion.id == id) {
                if (actual == cabeza && actual == cola) {
                    cabeza = cola = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) cabeza = actual.siguiente;
                    if (actual == cola) cola = actual.anterior;
                }
                return;
            }
            actual = actual.siguiente;
        } while (actual != cabeza);
    }

    public void mostrarCanciones() {
        if (cabeza == null) {
            System.out.println("La lista de canciones está vacía.");
            return;
        }
        CancionNodo actual = cabeza;
        do {
            System.out.println(actual.cancion);
            actual = actual.siguiente;
        } while (actual != cabeza);
    }
}

class Playlist {
    int id;
    String nombre;
    ListaDeCanciones canciones;

    public Playlist(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.canciones = new ListaDeCanciones();
    }
}

class NodoPlaylist {
    Playlist playlist;
    NodoPlaylist siguiente;

    public NodoPlaylist(Playlist playlist) {
        this.playlist = playlist;
        this.siguiente = null;
    }
}

class ListaDePlaylists {
    NodoPlaylist cabeza;

    public void agregarPlaylist(Playlist playlist) {
        NodoPlaylist nuevoNodo = new NodoPlaylist(playlist);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoPlaylist actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
    }

    public Playlist buscarPlaylist(int id) {
        NodoPlaylist actual = cabeza;
        while (actual != null) {
            if (actual.playlist.id == id) {
                return actual.playlist;
            }
            actual = actual.siguiente;
        }
        return null;
    }
}

class Reproductor {
    ListaDePlaylists playlists;
    CancionNodo actual; 

    public Reproductor() {
        this.playlists = new ListaDePlaylists();
    }

    public void cargarPlaylist(int idPlaylist) {
        Playlist playlist = playlists.buscarPlaylist(idPlaylist);
        if (playlist != null && playlist.canciones.cabeza != null) {
            actual = playlist.canciones.cabeza;
            System.out.println("Playlist cargada: " + playlist.nombre);
            System.out.println("Reproduciendo: " + actual.cancion);
        } else {
            System.out.println("Playlist no encontrada o vacía.");
        }
    }

    public void siguienteCancion() {
        if (actual != null) {
            actual = actual.siguiente;
            System.out.println("Reproduciendo: " + actual.cancion);
        } else {
            System.out.println("No hay canciones en reproducción.");
        }
    }

    public void cancionAnterior() {
        if (actual != null) {
            actual = actual.anterior;
            System.out.println("Reproduciendo: " + actual.cancion);
        } else {
            System.out.println("No hay canciones en reproducción.");
        }
    }
}

public class SimuladorDeMusica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Reproductor reproductor = new Reproductor();

        while (true) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Crear Playlist");
            System.out.println("2. Agregar Canción a Playlist");
            System.out.println("3. Cargar Playlist");
            System.out.println("4. Reproducir Siguiente Canción");
            System.out.println("5. Reproducir Canción Anterior");
            System.out.println("6. Salir");
            System.out.print("Selecciona una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Nombre de la playlist: ");
                    String nombrePlaylist = scanner.nextLine();
                    reproductor.playlists.agregarPlaylist(new Playlist(reproductor.playlists.cabeza == null ? 1 : reproductor.playlists.cabeza.playlist.id + 1, nombrePlaylist));
                    System.out.println("Playlist creada.");
                    break;
                case 2:
                    System.out.print("ID de la Playlist: ");
                    int idPlaylist = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nombre de la Canción: ");
                    String nombreCancion = scanner.nextLine();
                    System.out.print("Artista: ");
                    String artista = scanner.nextLine();
                    Playlist p = reproductor.playlists.buscarPlaylist(idPlaylist);
                    if (p != null) {
                        p.canciones.agregarCancion(new Cancion(idPlaylist, nombreCancion, artista));
                        System.out.println("Canción añadida.");
                    } else {
                        System.out.println("Playlist no encontrada.");
                    }
                    break;
                case 3:
                    System.out.print("ID de la Playlist a cargar: ");
                    int idCargar = scanner.nextInt();
                    reproductor.cargarPlaylist(idCargar);
                    break;
                case 4:
                    reproductor.siguienteCancion();
                    break;
                case 5:
                    reproductor.cancionAnterior();
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
