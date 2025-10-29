package ATM;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Usuario {
    private String nombre;
    private int id;
    private String password;
    private String date;
    private boolean bloqueado;


    public Usuario(String contra, String date, String nombre) {
        this.password = contra;
        this.id = (int) (Math.random() * (99999999 - 10000000) + 10000000); // ID aleatorio de 8 dígitos
        this.date = date;
        this.nombre = nombre;
        this.bloqueado = false;
    }

    @Override // Esto no se que cojones es pero se que tú sí, mucha suerte
    public String toString() {
        return "Nombre: " + this.nombre + ", ID: " + this.id + ", Contraseña: " + this.password + ", Fecha de nacimiento: " + this.date;
    }

    public static void PantallaInicio() { // Controlador general del resto del código, desde aquí salen el resto de operaciones
        Scanner sc = new Scanner(System.in);
        ArrayList<Usuario> cuentasNuevas = new ArrayList<>();
        Usuario LogIn;
        String op;
        while (true) {
            System.out.println("╔═════════════════════════╗");
            System.out.println("║                         ║");
            System.out.println("╠══ 1.-  Registrarse    ══╣");
            System.out.println("║                         ║");
            System.out.println("╠══ 2.- Iniciar sesión  ══╣");
            System.out.println("║                         ║");
            System.out.println("╚═════════════════════════╝");
            System.out.println("Seleccione la opción que desea:");
            op = sc.nextLine();
            if (op.equals("1")) {
                Usuario u = Registro(cuentasNuevas);
                cuentasNuevas.add(u);
                System.out.println("Su ID de inicio de sesión es: " + u.id);
            } else if (op.equals("2")) {
                if (!cuentasNuevas.isEmpty()) {
                    LogIn = InicioSesion(cuentasNuevas);
                    if (LogIn != null) {
                        System.out.println(LogIn);
                        break;
                    }

                } else {
                    System.out.println("No existen cuentas creadas actualmente.");
                }
            }
        }
    }

    public static Usuario Registro(ArrayList<Usuario> cuentas) { // Pide y comprueba los datos de registro de usuarios
        Scanner sc = new Scanner(System.in);
        String nombre;
        String fechaRegistro;
        String contraRegistro;
        boolean comprobarFecha;
        boolean comprobarContra;
        Usuario nuevologin;

        System.out.println("Ingresa el nombre completo del titular de la nueva cuenta: ");
        nombre = sc.nextLine();
        while (true) { // Bucle infinito hasta que la persona ponga una fecha correcta
            System.out.println("Ingresa la fecha de nacimiento del titular de la nueva cuenta(dd/mm/yyyy): ");
            fechaRegistro = sc.nextLine();
            comprobarFecha = ComprobarFecha(fechaRegistro);
            if (comprobarFecha) {
                break;
            }
        }
        while (true) { // Bucle que valida ID y contraseña y crea el usuario
            System.out.println("Introduce la contraseña del titular de la nueva cuenta: ");
            contraRegistro = sc.nextLine();
            comprobarContra = ComprobarContra(contraRegistro);
            if (comprobarContra) {
                nuevologin = new Usuario(contraRegistro, fechaRegistro, nombre);
                if (cuentas.contains(nuevologin.id)) {
                    while (cuentas.contains(nuevologin.id)) {
                        nuevologin.id = (int) (Math.random() * (99999999 - 10000000) + 10000000);
                    }
                }
                break;
            }
        }
        return nuevologin;
    }

    public static boolean ComprobarFecha(String fecha) { // Función que comprueba que la fecha sea real y correcta
        int anyo = Integer.parseInt(fecha.split("/")[2]);
        int mes = Integer.parseInt(fecha.split("/")[1]);

        if (anyo <= 2007) {
            /*Comprobación meses de 31 días*/
            if ((mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) && (Integer.parseInt(fecha.split("/")[0]) >= 1) && (Integer.parseInt(fecha.split("/")[0]) <= 31)) {
                return true;
                /*Comprobacion meses de 30 días*/
            } else if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && (Integer.parseInt(fecha.split("/")[0]) >= 1 || Integer.parseInt(fecha.split("/")[0]) <= 30)) {
                return true;
                /*Comprobación Febrero*/
            } else if (mes == 2 && (Integer.parseInt(fecha.split("/")[0]) >= 1 || Integer.parseInt(fecha.split("/")[0]) <= 28)) {
                return true;
            }
        }
        System.out.println("Fecha incorrecta");
        return false;
    }


    public static boolean ComprobarContra(String contra) { // Espresión regular para la contraseña pero paso por paso porque me gusta el dolor y la sangre y todas esas paranoias

        if (contra.length() < 8) {
            System.out.println("La contraseña debe contener al menos 8 caracteres");
            return false;
        }
        if (!Pattern.compile("[A-Z]").matcher(contra).find()) {
            System.out.println("La contraseña debe tener al menos una letra mayúscula");
            return false;
        }
        if (!Pattern.compile("[0-9]").matcher(contra).find()) {
            System.out.println("La contraseña debe tener al menos un número");
            return false;
        }
        if (!Pattern.compile("[a-z]").matcher(contra).find()) {
            System.out.println("La contraseña debe tener al menos una letra minúscula");
            return false;
        }
        if (!Pattern.compile("[@#€$%&!_*+?¿¡]").matcher(contra).find()) {
            System.out.println("La contraseña debe tener al menos un caracter especial (^@#€$%&!_-*-+?¿¡)");
            return false;
        }
        return true;
    }

    public static Usuario InicioSesion(ArrayList<Usuario> cuentas) { // Función que comprueba el ArrayList para enlazar la ID con la cuenta correspondiente y que sea posible iniciar sesión
        Scanner sc = new Scanner(System.in);
        int contadorCuentas = 0;
        boolean block;
        System.out.println("Introduce el ID de la cuenta a la que quiere acceder: ");
        int usuLogIn = Integer.parseInt(sc.nextLine());
        while (contadorCuentas != cuentas.size()) {
            for (Usuario usuario : cuentas) {
                if (usuario.id == (usuLogIn)) {
                    if (!usuario.bloqueado) { // Comprueba si la cuenta está bloqueada o no
                        block = CuentaBloqueada(usuario);
                        if (!block) {
                            return usuario;
                        }
                    } else {
                        System.out.println("El usuario está bloqueado, vamos espabilando que eres mayorcito.");
                        return null;
                    }
                }
                contadorCuentas++;
            }
        }
        System.out.println("Ese usuario no existe, inicie sesión con un ID válido.");
        return null;
    }

    public static boolean CuentaBloqueada(Usuario usuario) { // Pide la contraseña en el LogIn para ver si el usuario es tonto o no, si es tonto, le bloquea la cuenta. No queremos tontos en nuestro banco
        Scanner sc = new Scanner(System.in);
        String contraLogIn;
        for (int intentos = 2; intentos >= 0; intentos--) {
            System.out.println("Introduzca su contraseña: ");
            contraLogIn = sc.nextLine();
            if (contraLogIn.equals(usuario.password)) {
                System.out.println("Sesión iniciada");
                return false;
            } else {
                System.out.println("Contraseña incorrecta, vuelva a intentarlo");
                System.out.println("Le quedan "+intentos+" intentos");
            }
        }
        System.out.println("Se ha quedado sin intentos, el usuario ha sido bloqueado.");
        usuario.bloqueado = true; // Bloquea la cuenta al quedarse sin intentos
        return true;
    }
}
