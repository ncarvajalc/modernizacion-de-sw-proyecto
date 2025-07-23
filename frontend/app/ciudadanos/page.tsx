"use client"

import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import axios from "axios";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

// Zod schema for validation
const citizenSchema = z.object({
  tipoDeIdentificacion: z.enum(["CC", "PermisoEspecial", "TI"]),
  identificacionCiudadano: z.string().min(1, "La identificación es requerida"),
  nombreCiudadano: z.string().min(1, "El nombre es requerido"),
  apellidoCiudadano: z.string().min(1, "El apellido es requerido"),
  esVacunable: z.enum(["1", "0"]),
  fechaDeNacimiento: z.string().min(1, "La fecha de nacimiento es requerida"),
  telefonoDeContacto: z.string().min(1, "El teléfono es requerido"),
  estadoVacunacion: z.enum(["1", "2", "3"]),
  etapa: z.enum(["1", "2", "3"]),
  eps: z.enum(["Sanitas-10", "Compensar-12"]),
  genero: z.enum(["Masculino", "Femenino", "Otro"]),
  rol: z.enum(["Paciente", "Médico", "Enfermero", "Administrativo"]),
  ciudad: z.string().min(1, "La ciudad es requerida"),
  localidad: z.string().min(1, "La localidad es requerida"),
});

type CitizenFormData = z.infer<typeof citizenSchema>;

export default function Page() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitMessage, setSubmitMessage] = useState<{
    type: "success" | "error";
    text: string;
  } | null>(null);

  const form = useForm<CitizenFormData>({
    resolver: zodResolver(citizenSchema),
    defaultValues: {
      tipoDeIdentificacion: "CC",
      esVacunable: "1",
      estadoVacunacion: "1",
      etapa: "2",
      eps: "Sanitas-10",
      genero: "Masculino",
      rol: "Paciente",
    },
  });

  const onSubmit = async (data: CitizenFormData) => {
    setIsSubmitting(true);
    setSubmitMessage(null);

    try {
      // Parse EPS data
      const [nombreOficina, regionOficina] = data.eps.split("-");
      
      // Convert form data to API format
      const apiData = {
        tipoDeIdentificacion: data.tipoDeIdentificacion,
        identificacionCiudadano: data.identificacionCiudadano,
        nombreCiudadano: data.nombreCiudadano,
        apellidoCiudadano: data.apellidoCiudadano,
        esVacunable: data.esVacunable,
        fechaDeNacimiento: new Date(data.fechaDeNacimiento).toISOString(),
        telefonoDeContacto: parseInt(data.telefonoDeContacto),
        estadoVacunacion: parseInt(data.estadoVacunacion),
        etapa: parseInt(data.etapa),
        nombreOficina: nombreOficina,
        regionOficina: parseInt(regionOficina),
        genero: data.genero,
        rol: data.rol,
        ciudad: data.ciudad,
        localidad: data.localidad,
      };

      const response = await axios.post(
        "http://localhost:8000/api/v1/ciudadanos",
        apiData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      setSubmitMessage({
        type: "success",
        text: "Ciudadano registrado exitosamente",
      });
      
      // Reset form on success
      form.reset();

    } catch (error) {
      console.error("Error submitting form:", error);
      setSubmitMessage({
        type: "error",
        text: "Error al registrar el ciudadano. Por favor, intente nuevamente.",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="font-sans min-h-screen p-8 max-w-4xl mx-auto">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-4">Registro de Ciudadanos</h1>
        <p className="text-lg text-gray-600">
          Complete el formulario para registrar un nuevo ciudadano en el sistema de vacunación.
        </p>
      </div>

      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Tipo de Identificación */}
            <FormField
              control={form.control}
              name="tipoDeIdentificacion"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tipo de Identificación</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="CC">Cédula de Ciudadanía</option>
                      <option value="PermisoEspecial">Permiso Especial</option>
                      <option value="TI">Tarjeta de Identidad</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Identificación */}
            <FormField
              control={form.control}
              name="identificacionCiudadano"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Número de Identificación</FormLabel>
                  <FormControl>
                    <Input placeholder="1234567890" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Nombre */}
            <FormField
              control={form.control}
              name="nombreCiudadano"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nombre</FormLabel>
                  <FormControl>
                    <Input placeholder="Juan" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Apellido */}
            <FormField
              control={form.control}
              name="apellidoCiudadano"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Apellido</FormLabel>
                  <FormControl>
                    <Input placeholder="Pérez" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Es Vacunable */}
            <FormField
              control={form.control}
              name="esVacunable"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>¿Es Vacunable?</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="1">Sí</option>
                      <option value="0">No</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Fecha de Nacimiento */}
            <FormField
              control={form.control}
              name="fechaDeNacimiento"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Fecha de Nacimiento</FormLabel>
                  <FormControl>
                    <Input type="date" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Teléfono */}
            <FormField
              control={form.control}
              name="telefonoDeContacto"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Teléfono de Contacto</FormLabel>
                  <FormControl>
                    <Input placeholder="3001234567" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Estado de Vacunación */}
            <FormField
              control={form.control}
              name="estadoVacunacion"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Estado de Vacunación</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="1">No Vacunado</option>
                      <option value="2">Parcialmente Vacunado</option>
                      <option value="3">Completamente Vacunado</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Etapa */}
            <FormField
              control={form.control}
              name="etapa"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Etapa</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="1">Etapa 1</option>
                      <option value="2">Etapa 2</option>
                      <option value="3">Etapa 3</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* EPS */}
            <FormField
              control={form.control}
              name="eps"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>EPS</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="Sanitas-10">Sanitas</option>
                      <option value="Compensar-12">Compensar</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Género */}
            <FormField
              control={form.control}
              name="genero"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Género</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="Masculino">Masculino</option>
                      <option value="Femenino">Femenino</option>
                      <option value="Otro">Otro</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Rol */}
            <FormField
              control={form.control}
              name="rol"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Rol</FormLabel>
                  <FormControl>
                    <select
                      {...field}
                      className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-xs transition-colors placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm"
                    >
                      <option value="Paciente">Paciente</option>
                      <option value="Médico">Médico</option>
                      <option value="Enfermero">Enfermero</option>
                      <option value="Administrativo">Administrativo</option>
                    </select>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Ciudad */}
            <FormField
              control={form.control}
              name="ciudad"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Ciudad</FormLabel>
                  <FormControl>
                    <Input placeholder="Bogotá" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Localidad */}
            <FormField
              control={form.control}
              name="localidad"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Localidad</FormLabel>
                  <FormControl>
                    <Input placeholder="Chapinero" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          {/* Submit Message */}
          {submitMessage && (
            <div
              className={`p-4 rounded-md ${
                submitMessage.type === "success"
                  ? "bg-green-50 text-green-800 border border-green-200"
                  : "bg-red-50 text-red-800 border border-red-200"
              }`}
            >
              {submitMessage.text}
            </div>
          )}

          {/* Submit Button */}
          <div className="flex justify-end pt-6">
            <Button type="submit" disabled={isSubmitting} className="px-8">
              {isSubmitting ? "Registrando..." : "Registrar Ciudadano"}
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
