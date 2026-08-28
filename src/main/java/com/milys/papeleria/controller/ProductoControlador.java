package com.milys.papeleria.controller;

import com.milys.papeleria.model.Producto;
import com.milys.papeleria.repository.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping
public class ProductoControlador {

    @Autowired
    private ProductoRepositorio repositorio;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String listarProductos(Model modelo) {
        modelo.addAttribute("productos", repositorio.findAll());
        return "Index";
    }

    @GetMapping("/lista")
    public String listarP(Model modelo) {
        modelo.addAttribute("productos", repositorio.findAll());
        return "lista_productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "formulario_producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, @RequestParam("fileinput") MultipartFile archivo) {
        if (!archivo.isEmpty()) {
            try {
                Path directorioPath = Paths.get(uploadPath);
                if (!Files.exists(directorioPath)) {
                    Files.createDirectories(directorioPath);
                }
                String nombreImagen = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Path rutaCompleta = directorioPath.resolve(nombreImagen);
                Files.copy(archivo.getInputStream(), rutaCompleta);
                producto.setRutaImage(nombreImagen);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/lista?error";
            }
        }
        repositorio.save(producto);
        return "redirect:/lista";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("producto", repositorio.findById(id).orElse(null));
        return "formulario_producto";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        repositorio.deleteById(id);
        return "redirect:/lista";
    }
}
