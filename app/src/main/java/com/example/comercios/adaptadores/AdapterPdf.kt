package com.example.comercios.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.example.comercios.R
import com.example.comercios.adaptadores.AdapterPdf.*
import com.example.comercios.modelos.ModeloPdf
import kotlinx.android.synthetic.main.template_rv_pdf.view.*
import kotlinx.android.synthetic.main.template_rvpromo.view.*

class AdapterPdf(var arraydatos:ArrayList<ModeloPdf>): RecyclerView.Adapter<PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPdf.PdfViewHolder =
        PdfViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.template_rv_pdf, parent, false))

    override fun getItemCount(): Int = arraydatos.size

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdfLista = arraydatos[position]
        holder.itemView.tv_tem_pdf_cantidad.text = pdfLista.cantidad.toString()
        holder.itemView.tv_tem_pdf_producto.text = pdfLista.producto
        holder.itemView.tv_tem_pdf_precio.text = pdfLista.precio.toString()
        holder.itemView.tv_tem_pdf_subtotal.text = pdfLista.subTotal.toString()
    }
}