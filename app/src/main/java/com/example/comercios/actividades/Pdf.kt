package com.example.comercios.actividades

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comercios.R
import com.example.comercios.adaptadores.AdapterPdf
import com.example.comercios.modelos.ModeloPdf
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_pdf.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Pdf : AppCompatActivity() {

    var adaptadorPdf:AdapterPdf? = null
    var layoutManager:RecyclerView.LayoutManager? = null
    var arrayDatosRecyclerPdf:ArrayList<ModeloPdf>? = null
    companion object{
        const val PROD_SELECT = "ProductosSelect"
    }
    private val STORAGE_CODE: Int = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        arrayDatosRecyclerPdf = intent?.getSerializableExtra(PROD_SELECT) as ArrayList<ModeloPdf>?
        val extraerPrecios: Intent = intent
        var preciosTotales = extraerPrecios.getStringExtra("Total Precios")
        tvTotalPresupuesto.text = preciosTotales
        // inflaré el recyclerPdf
        recyclerPdf?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerPdf?.layoutManager =layoutManager
        adaptadorPdf = AdapterPdf(arrayDatosRecyclerPdf!!)
        recyclerPdf?.adapter = adaptadorPdf

        guardarPdf?.setOnClickListener {
            //necesitamos manejar permisos de tiempo de ejecución para dispositivos con marshmallow  y superior
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                //sistema operativo> = marshMallow (6.0), verifique que el permiso esté habilitado o no
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    // no se otorgó permiso, solicítelo
                    val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                }
                else{
                    //permiso ya otorgado, llamar al método savepdf
                    savePdf()
                }
            }
            else{
                //permiso ya otorgado, llamar al método savepdf
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    savePdf()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun savePdf(){
        // crear objeto de la clase document
        val mDoc = Document()
        // pdf. nombre del archivo
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".Pdf"
        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()

            val mPrecioTotal = tvTotalPresupuesto.text.toString()


            mDoc.addAuthor("Hernan Torres")

            val table = PdfPTable(4)

            table.headerRows = 1
            table.addCell(
                PdfPCell(
                    Phrase("Producto", Font(
                        Font.FontFamily.HELVETICA, 16f,
                        Font.BOLD)
                    )
                )
            )
            table.addCell(
                PdfPCell(
                    Phrase("Cantidad", Font(
                        Font.FontFamily.HELVETICA, 16f,
                        Font.BOLD)
                    )
                )
            )
            table.addCell(
                PdfPCell(
                    Phrase("Precio", Font(
                        Font.FontFamily.HELVETICA, 16f,
                        Font.BOLD)
                    )
                )
            )
            table.addCell(
                PdfPCell(
                    Phrase("Subtotal", Font(
                        Font.FontFamily.HELVETICA, 16f,
                        Font.BOLD)
                    )
                )
            )

            for (list in arrayDatosRecyclerPdf!!){
                table.addCell(PdfPCell(Phrase(list.cantidad.toString(), Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(PdfPCell(Phrase(list.producto, Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(PdfPCell(Phrase(list.precio.toString(), Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(PdfPCell(Phrase(list.subTotal.toString(), Font(Font.FontFamily.HELVETICA, 12f))))

            }


            mDoc.add(table)
            val preT = Paragraph(mPrecioTotal, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD))
            preT.alignment = Element.ALIGN_RIGHT
            mDoc.add(preT)

            mDoc.close()
            Toast.makeText(this, " $mFileName.pdf\nse guardó en \n$mFilePath", Toast.LENGTH_SHORT).show()

        }
        catch (e: Exception){}

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // se otorgó el permiso de la ventana emergente, llama a savePdf()
                    savePdf()
                }
                else {
                    // se denegó el permiso de la ventana emergente, muestra mensaje de error
                    Toast.makeText(this, "permiso denegado", Toast.LENGTH_SHORT).show()


                }
            }
        }
        // esto en el activity principal
    }
}