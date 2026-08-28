package com.milys.papeleria.service;

import com.milys.papeleria.model.ItemCarrito;
import com.milys.papeleria.model.Producto;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class CarritoService {

    private static final String CARRITO_SESSION_KEY = "carrito";

    @SuppressWarnings("unchecked")
    public Map<Long, ItemCarrito> obtenerCarrito(HttpSession session) {
        Map<Long, ItemCarrito> carrito = (Map<Long, ItemCarrito>) session.getAttribute(CARRITO_SESSION_KEY);
        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute(CARRITO_SESSION_KEY, carrito);
        }
        return carrito;
    }

    public void agregarProducto(HttpSession session, Producto producto, int cantidad) {
        Map<Long, ItemCarrito> carrito = obtenerCarrito(session);
        if (carrito.containsKey(producto.getId())) {
            ItemCarrito item = carrito.get(producto.getId());
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            carrito.put(producto.getId(), new ItemCarrito(producto, cantidad));
        }
        session.setAttribute(CARRITO_SESSION_KEY, carrito);
    }

    public void eliminarProducto(HttpSession session, Long productoId) {
        Map<Long, ItemCarrito> carrito = obtenerCarrito(session);
        carrito.remove(productoId);
        session.setAttribute(CARRITO_SESSION_KEY, carrito);
    }

    public double calcularTotal(HttpSession session) {
        Map<Long, ItemCarrito> carrito = obtenerCarrito(session);
        return carrito.values().stream().mapToDouble(ItemCarrito::getTotal).sum();
    }
}
