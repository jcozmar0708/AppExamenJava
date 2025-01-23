package es.iesoretania.appexamen.Miscelanea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "calificaciones";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_ALUMNOS = "alumnos";
    private static final String COLUMN_ID_ALUMNO = "idalumno";
    private static final String COLUMN_CONTRASENIA = "password";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDOS = "apellidos";
    private static final String COLUMN_SEXO = "sexo";

    private static final String TABLE_NAME_NOTAS = "notas";
    private static final String COLUMN_MATEMATICAS = "matematicas";
    private static final String COLUMN_LENGUA = "lengua";
    private static final String COLUMN_INFORMATICA = "informatica";
    private static final String COLUMN_INGLES = "ingles";

    private static final String CREATE_TABLE_ALUMNOS =
            "CREATE TABLE " + TABLE_NAME_ALUMNOS + " (" +
                    COLUMN_ID_ALUMNO + " TEXT PRIMARY KEY, " +
                    COLUMN_CONTRASENIA + " TEXT NOT NULL," +
                    COLUMN_NOMBRE + " TEXT NOT NULL, " +
                    COLUMN_APELLIDOS + " TEXT NOT NULL, " +
                    COLUMN_SEXO + " TEXT NOT NULL" +
                    ");";

    private static final String CREATE_TABLE_NOTAS =
            "CREATE TABLE " + TABLE_NAME_NOTAS + " (" +
                    COLUMN_ID_ALUMNO + " TEXT PRIMARY KEY, " +
                    COLUMN_MATEMATICAS + " NUMERIC NOT NULL," +
                    COLUMN_LENGUA + " NUMERIC NOT NULL, " +
                    COLUMN_INFORMATICA + " NUMERIC NOT NULL, " +
                    COLUMN_INGLES + " NUMERIC NOT NULL" +
                    ");";

    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ALUMNOS);
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALUMNOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTAS);
        onCreate(sqLiteDatabase);
    }

    public long insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AdminSQLiteOpenHelper.COLUMN_ID_ALUMNO, usuario.getUsuario());
        contentValues.put(AdminSQLiteOpenHelper.COLUMN_CONTRASENIA, usuario.getContrasenia());
        contentValues.put(AdminSQLiteOpenHelper.COLUMN_NOMBRE, usuario.getNombre());
        contentValues.put(AdminSQLiteOpenHelper.COLUMN_APELLIDOS, usuario.getApellidos());
        contentValues.put(AdminSQLiteOpenHelper.COLUMN_SEXO, usuario.getSexo());

        return db.insert(AdminSQLiteOpenHelper.TABLE_NAME_ALUMNOS, null, contentValues);
    }

    public boolean usuarioExiste(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_ALUMNOS + " WHERE " + COLUMN_ID_ALUMNO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario});

        return cursor.moveToFirst();
    }

    public static class Usuario {
        private final String usuario;
        private final String contrasenia;
        private final String nombre;
        private final String apellidos;
        private final String sexo;

        public Usuario(String usuario, String contrasenia, String nombre, String apellidos, String sexo) {
            this.usuario = usuario;
            this.contrasenia = contrasenia;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.sexo = sexo;
        }

        public String getUsuario() {
            return usuario;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public String getSexo() {
            return sexo;
        }
    }
}
