package com.milys.papeleria.controller;

import com.milys.papeleria.model.Producto;
import com.milys.papeleria.repository.ProductoRepositorio;
import com.milys.papeleria.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoRepositorio productoRepository;

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        model.addAttribute("carrito", carritoService.obtenerCarrito(session).values());
        model.addAttribute("total", carritoService.calcularTotal(session));
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long id, @RequestParam(defaultValue = "1") int cantidad, HttpSession session) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            carritoService.agregarProducto(session, producto, cantidad);
        }
        return "redirect:/carrito";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        carritoService.eliminarProducto(session, id);
        return "redirect:/carrito";
    }
}
