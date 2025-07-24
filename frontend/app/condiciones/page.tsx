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
const conditionSchema = z.object({
  descripcionCondicion: z.string().min(1, "La descripción de la condición es requerida"),
});

type ConditionFormData = z.infer<typeof conditionSchema>;

export default function Page() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitMessage, setSubmitMessage] = useState<{
    type: "success" | "error";
    text: string;
  } | null>(null);

  const form = useForm<ConditionFormData>({
    resolver: zodResolver(conditionSchema),
    defaultValues: {
      descripcionCondicion: "",
    },
  });

  const onSubmit = async (data: ConditionFormData) => {
    setIsSubmitting(true);
    setSubmitMessage(null);

    try {
      const response = await axios.post(
        "http://localhost:8000/api/v1/condiciones",
        {
          descripcionCondicion: data.descripcionCondicion,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      setSubmitMessage({
        type: "success",
        text: "Condición creada exitosamente",
      });
      
      // Reset form on success
      form.reset();

    } catch (error) {
      console.error("Error submitting form:", error);
      setSubmitMessage({
        type: "error",
        text: "Error al crear la condición. Por favor, intente nuevamente.",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="font-sans min-h-screen p-8 max-w-4xl mx-auto">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-4">Gestión de Condiciones</h1>
        <p className="text-lg text-gray-600">
          Crea nuevas condiciones médicas que pueden afectar el proceso de vacunación.
        </p>
      </div>

      <div className="max-w-2xl">
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            {/* Descripción de la Condición */}
            <FormField
              control={form.control}
              name="descripcionCondicion"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Descripción de la Condición</FormLabel>
                  <FormControl>
                    <Input 
                      placeholder="Ej: Diabetes tipo 2, Hipertensión arterial, Asma..." 
                      {...field} 
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

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
                {isSubmitting ? "Creando..." : "Crear Condición"}
              </Button>
            </div>
          </form>
        </Form>
      </div>
    </div>
  );
} 