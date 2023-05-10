package com.example.gatote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    //Inicializacion de todos los botones, textviews que se usan en el juego
    lateinit var buttons: Array<Array<Button>>
    lateinit var textViewJugador1: TextView
    lateinit var textViewJugador2: TextView

    //variables para asignación de turnos y puntuacion de los jugadores, numero de rounds
    private var jugador1Turno: Boolean = true
    private var roundCount: Int = 0
    private var jugador1Puntos: Int = 0
    private var jugador2Puntos: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Llamada de textviews
        textViewJugador1 = findViewById(R.id.jugador1TextView)
        textViewJugador2 = findViewById(R.id.jugador2TextView)
        //botones dimensionales
        buttons = Array(3) { r ->
            Array(3) { c ->
                initButtons(r, c)
            }
        }
        //Boton reset para reiniciar la puntuacion y la partida
        val btnReset: Button = findViewById(R.id.btnReset)
        btnReset.setOnClickListener {
            jugador1Puntos=0
            jugador2Puntos=0
            actualizarPuntuacion()
            limpiarTablero()
        }
    }

    //Inicializacion de botones, desde aqui  se llama la funcion que hace funcionar los botones
    private fun initButtons(r: Int, c: Int): Button {
        //encontrar el boton utilizando findview
        val btn: Button = findViewById(resources.getIdentifier("btn$r$c", "id", packageName))
        btn.setOnClickListener {
            onBtnClick(btn)
        }
        return btn
    }

    // Se pone una X o una O en el boton dependiendo de quien sea el turno
    private fun onBtnClick(btn: Button) {
        // Se revisa que no se haya colocado ninguna imagen (X u O)
        if (btn.text != "") {
            return
        }
        //Entre turnos se alterna los colores del texto para distinguirlos mejor
        if (jugador1Turno) {
            btn.setTextColor(ContextCompat.getColor(this, R.color.tache))
            btn.text="X"
        } else {
            btn.setTextColor(ContextCompat.getColor(this, R.color.white))
            btn.text="O"
        }
        roundCount++ // se aumenta el numero de rondas

        // Después de cada jugada, se revisa si alguno de los jugadores ha ganado dependiendo de quien tenga el turno
        if (revisarGanador()) {
            if (jugador1Turno) {
                gano(1)
            } else {
                gano(2)
            }
        } else if (roundCount == 9) { // si las rondas alcanzan 9, entonces se declara un empate
            empate()
        } else {
            jugador1Turno = !jugador1Turno // se cambia el turno de jugador
        }
    }
    //en esta funcion se revisa si hay un ganador haciendo que los espacios sean un array y se verifiquen
    private fun revisarGanador(): Boolean {
        val fields = Array(3) { r ->
            Array(3) { c ->
                buttons[r][c].text
            }
        }
        //Se revisa si coinciden 3 campos para declarar un ganador
        for (i in 0..2) { //filas
            if ((fields[i][0] == fields[i][1]) &&
                (fields[i][0] == fields[i][2]) &&
                (fields[i][0] != "")
            ) return true
        }

        for (i in 0..2) { //columnas
            if ((fields[0][i] == fields[1][i]) &&
                (fields[0][i] == fields[2][i]) &&
                (fields[0][i] != "")
            ) return true
        }

        if ( //diagonal izq
            (fields[0][0] == fields[1][1]) &&
            (fields[0][0] == fields[2][2]) &&
            (fields[0][0] != "")
        ) return true

        if ( //diagonal der
            (fields[0][2] == fields[1][1]) &&
            (fields[0][2] == fields[2][0]) &&
            (fields[0][2] != "")
        ) return true

        return false

    }

    //Una vez que se tenga un ganador, se le suma puntos a su marcador con esta funcion
    private fun gano(jugador: Int) {
        if(jugador==1) jugador1Puntos++ else jugador2Puntos++
        Toast.makeText(applicationContext, "El jugador $jugador gano!", Toast.LENGTH_SHORT).show()
        actualizarPuntuacion() //Se actualiza la puntuación de los jugadores en la interfaz
        limpiarTablero() //Se limpia el tablero para poder volver a jugar
    }
    //Esta funcion limpia los botones pasando por cada uno de ellos con un ciclo
    private fun limpiarTablero() {
        for (i in 0..2){
            for (j in 0..2){
                buttons[i][j].text=""
            }
        }
        roundCount=0 //se reinicia el contador de rondas
        jugador1Turno=true //Y se vuelve a asignar el turno al jugador 1
    }
    //Esta funcion actualiza la interfaz para mostrar las puntuaciones de los jugadores
    private fun actualizarPuntuacion() {
        textViewJugador1.text="Jugador 1: $jugador1Puntos"
        textViewJugador2.text="Jugador 2: $jugador2Puntos"
    }

    //Si nadie gano, simplemente se avisa que nadie ha ganado con un mensaje
    private fun empate() {
        Toast.makeText(applicationContext, "Partida Empatada!", Toast.LENGTH_SHORT).show()
        limpiarTablero()
    }
}