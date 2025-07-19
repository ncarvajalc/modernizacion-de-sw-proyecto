import React from "react";

export default async function Page({
  params,
}: {
  params: { idCiudadano: string };
}) {
  const { idCiudadano } = await params;
  return (
    <div className="font-sans grid grid-rows-[20px_1fr_20px] items-center justify-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 max-w-screen-lg mx-auto">
      <main className="flex flex-col gap-[32px] row-start-2 items-center sm:items-start">
        <h1 className="text-4xl font-bold">
          Citas del ciudadano {idCiudadano}
        </h1>
      </main>
    </div>
  );
}
