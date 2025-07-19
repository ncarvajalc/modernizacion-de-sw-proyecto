import React from "react";

export default function Page() {
  return (
    <div className="font-sans grid grid-rows-[20px_1fr_20px] items-center justify-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 max-w-screen-lg mx-auto">
      <main className="flex flex-col gap-[32px] row-start-2 items-center sm:items-start">
        <h1 className="text-4xl font-bold">Registro de Ciudadanos</h1>
        <div className="flex flex-col gap-4">
          <h2 className="text-2xl font-bold">
            Registra a los ciudadanos que se vacunarán.
          </h2>
        </div>
      </main>
    </div>
  );
}
