package com.developerscracks.sivapp.ui.reportes.view

import android.app.Activity
import android.graphics.Color
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developerscracks.sivapp.data.model.venta.DatosVentas
import com.developerscracks.sivapp.data.network.RetrofitClient
import com.developerscracks.sivapp.utils.Permisos
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.math.roundToInt

class ReportesViewModel: ViewModel() {
    private var _listaVentas = MutableLiveData<List<DatosVentas>>()
    val listaVentas: LiveData<List<DatosVentas>> get() = _listaVentas

    private var _mensaje = MutableLiveData<String>()
    val mensaje:LiveData<String> get() = _mensaje

    private var _accion = MutableLiveData<String>()
    val accion: LiveData<String> get() = _accion

    fun checkEscrituraPermiso(activity: Activity): Boolean{
        val isPermiso = Permisos().checkEscrituraPermiso(activity)
        return if (isPermiso){
            true
        }else{
            _mensaje.value = "Permiso de escritura rechazado"
            false
        }
    }

    fun getVentasPeriodo(fechaInicio: String, fechaFinal: String, accion: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.webService.getVentasPeriodo(fechaInicio, fechaFinal)
            withContext(Dispatchers.Main){
                if (response.body()!!.codigo == "200"){
                    if (response.body()!!.resultado.isNotEmpty()){
                        _listaVentas.value = response.body()!!.resultado
                        _accion.value = accion
                    }else{
                        _mensaje.value = response.body()!!.mensaje
                    }
                }
            }
        }
    }

    fun graficarBarras(grafica: BarGraph){
        val puntos = ArrayList<Bar>()

        val aux = ArrayList<String>()
        listaVentas.value!!.forEach{
            aux.add(it.fechaVenta.substring(0..9))
        }

        val auxLista = aux.toSet()
        auxLista.forEach { auxElemento ->
            val barra = Bar()
            barra.color = Color.parseColor(generarColorHexAleatorio())
            barra.name = auxElemento

            var totalVentaDia = 0.0
            listaVentas.value!!.forEach { elemento ->
                if (auxElemento == elemento.fechaVenta.substring(0..9)){
                    totalVentaDia += elemento.total
                }
            }

            barra.value = totalVentaDia.toFloat()

            puntos.add(barra)
        }

        grafica.bars = puntos
    }

    fun generarColorHexAleatorio():String{
        val letras = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var color = "#"
        for (i in 0 .. 5){
            color += letras[(Math.random() * 15).roundToInt()]
        }
        return color
    }

    fun generarReportePDF(fechaInicio: String, fechaFinal: String){
        fechaInicio.substring(0..9)
        fechaFinal.substring(0..9)

        val NOMBRE_DIRECTORIO = "ReportesSIVApp"
        val NOMBRE_DOCUMENTO = "Reporte_${fechaInicio}_${fechaFinal}.pdf"

        var totalVentasReporte = 0.0

        try {
            val ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+"/"+NOMBRE_DIRECTORIO
            val dir = File(ruta)

            if (!dir.exists()){
                dir.mkdir()
            }

            val file = File(dir, NOMBRE_DOCUMENTO)
            val fos = FileOutputStream(file)

            val documento = Document()
            PdfWriter.getInstance(documento,fos)

            documento.open()

            val titulo = Paragraph(
                "REPORTE DE VENTAS\n$fechaInicio a $fechaFinal\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLACK)
            )

            documento.add(titulo)

            val tabla = PdfPTable(3)
            tabla.addCell("FECHA VENTA")
            tabla.addCell("ID VENTA")
            tabla.addCell("TOTAL VENTA")

            listaVentas.value!!.forEach {
                tabla.addCell(it.fechaVenta.substring(0..9))
                tabla.addCell(it.idVenta)
                tabla.addCell(it.total.toString())

                totalVentasReporte += it.total
            }

            documento.add(tabla)

            val totalVentaRep = Paragraph(
                "Total ventas reporte: $totalVentasReporte",
                FontFactory.getFont("arial", 12f, Font.BOLD, BaseColor.BLACK)
            )

            documento.add(totalVentaRep)

            _mensaje.value = "El reporte se creo exitosamente"

            documento.close()
        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }catch (e: DocumentException){
            e.printStackTrace()
        }
    }
}