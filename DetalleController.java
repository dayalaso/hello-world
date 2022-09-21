package com.taller.proyecto.controlador;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.taller.proyecto.PaginationCriteria;
import com.taller.proyecto.dao.DetalleDao;
import com.taller.proyecto.modelos.Detalle;
import com.taller.proyecto.servicio.UsuarioServicio;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detalle")
public class DetalleController {

    @Resource
    DetalleDao detalleDao;
    @Resource
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model, HttpServletRequest req) {
        if (usuarioServicio.verificar(req.getSession()))
            return "detalle/index";
        else
            return "/sinsession";
    }

    @GetMapping("/lista")
    public String lista(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model, HttpServletRequest req) {
        return "detalle/lista";
    }

    @PostMapping("/List")
    public @ResponseBody
    Map<?, ?> getAlmacenData(@RequestBody PaginationCriteria treq, String tiempo, HttpServletRequest req) {
        System.out.println("valor de tipotransaccion: " + treq.getTipotran());
        if (Integer.parseInt(treq.getTipotran()) > -1) {
            System.out.println("entro en reporte por mes" + treq.getTipotran());
            //if(usuarioServicio.verificar(req)) {
            String total = "0";
            String filtrado = "0";
            Map<String, Object> Data = new HashMap<String, Object>();
            String search = treq.getSearch().getValue();
            List<Map<String, Object>> lista = detalleDao.find(treq.getStart(), search, treq.getLength(), treq.getOrder().get(0).getColumn() + 1, treq.getOrder().get(0).getDir(), treq.getTipotran(), treq.getTiempo(), treq.getFechainicio(), treq.getFechafin());
            if (lista.size() > 0) {
                total = lista.get(0).get("total").toString();
                filtrado = lista.get(0).get("filtrado").toString();
            }

            Data.put("draw", treq.getDraw());
            Data.put("recordsTotal", total);
            Data.put("data", lista);
            Data.put("recordsFiltered", filtrado);
            return Data;
        } else {
            if (Integer.parseInt(treq.getTipotran()) > -2) {
                System.out.println("entro en reporte prod mas vendido" + treq.getTipotran());
                String total = "0";
                String filtrado = "0";
                Map<String, Object> Data = new HashMap<String, Object>();
                String search = treq.getSearch().getValue();
                List<Map<String, Object>> lista = detalleDao.listaprodmasven(treq.getStart(), search, treq.getLength(), treq.getOrder().get(0).getColumn() + 1, treq.getOrder().get(0).getDir());
                if (lista.size() > 0) {
                    total = lista.get(0).get("total").toString();
                    filtrado = lista.get(0).get("filtrado").toString();
                }

                Data.put("draw", treq.getDraw());
                Data.put("recordsTotal", total);
                Data.put("data", lista);
                Data.put("recordsFiltered", filtrado);
                return Data;
            } else {
                if (Integer.parseInt(treq.getTipotran()) > -3) {
                    System.out.println("entro en reporte prod mas vendido" + treq.getTipotran());
                    System.out.println("entro en reporte prod menos vendido" + treq.getTipotran());
                    String total = "0";
                    String filtrado = "0";
                    Map<String, Object> Data = new HashMap<String, Object>();
                    String search = treq.getSearch().getValue();
                    List<Map<String, Object>> lista = detalleDao.listaprodmenosven(treq.getStart(), search, treq.getLength(), treq.getOrder().get(0).getColumn() + 1, treq.getOrder().get(0).getDir());
                    if (lista.size() > 0) {
                        total = lista.get(0).get("total").toString();
                        filtrado = lista.get(0).get("filtrado").toString();
                    }

                    Data.put("draw", treq.getDraw());
                    Data.put("recordsTotal", total);
                    Data.put("data", lista);
                    Data.put("recordsFiltered", filtrado);
                    return Data;
                } else {

                    System.out.println("entro en reporte prod escasos" + treq.getTipotran());
                    String total = "0";
                    String filtrado = "0";
                    Map<String, Object> Data = new HashMap<String, Object>();
                    String search = treq.getSearch().getValue();
                    List<Map<String, Object>> lista = detalleDao.listaprodesca(treq.getStart(), search, treq.getLength(), treq.getOrder().get(0).getColumn() + 1, treq.getOrder().get(0).getDir());
                    if (lista.size() > 0) {
                        total = lista.get(0).get("total").toString();
                        filtrado = lista.get(0).get("filtrado").toString();
                    }

                    Data.put("draw", treq.getDraw());
                    Data.put("recordsTotal", total);
                    Data.put("data", lista);
                    Data.put("recordsFiltered", filtrado);
                    return Data;
                }
            }

        }

    }


    //https://localhost:8080/proyecto/persona/obtener/10
    @PostMapping("/obtener/{idtransaccion}")
    public @ResponseBody
    Map<String, Object> obtener(@PathVariable Integer idtransaccion) {

		//ejempli


        return detalleDao.obtener(idtransaccion).get(0);


    }
    @PostMapping("/listasimple")
	public @ResponseBody
	List<Map<String, Object>> listasimple(){
		return detalleDao.listaSimple();
	}

    @PostMapping("/crear")
    public @ResponseBody
    String create(@RequestBody Detalle emp, HttpServletRequest req) {
        //if(usuarioServicio.verificar(req)) {
        if (emp.getIdtransaccion() == 0)// si el id es cero, se entiende que el usaurio es nuevo
            detalleDao.insert(emp);
        else
            detalleDao.update(emp);// si el id es disstinto a cero, se entiende que el usaurio no es neuvo y se llama a la funcion de actualizar
        return "{status:true}";
        //}
        //else return null;
    }

    @DeleteMapping("/deleteById")
    public @ResponseBody
    String delete(@RequestBody String idtransaccion, HttpServletRequest req) {
        //if(usuarioServicio.verificar(req)) {
        JSONObject o = new JSONObject(idtransaccion);
        detalleDao.delete(Integer.parseInt(o.getString("idtransaccion")));
        return "{status:true}";
        //}else
        //	return null;
    }

}
