# PM Products API - Descripción Técnica

# Rama 
En esta rama se invoca el API Rest con inyección de dependencias. Se colapsa el el detalle del producto permitiendo expandirlo para ver más
información.
También tenemo una pantalla de navegación que permite movernos entre la página principal, el listado de productos y favoritos.

# Objetivo

Este documento detalla la arquitectura técnica y el funcionamiento interno de la aplicación `pm_products_api`. 
La app consume la API pública de `dummyjson` para obtener y mostrar una lista de productos, permitiendo al usuario marcarlos como favoritos
en una base de datos.
Utiliza Room para la persistencia de datos y Retrofit para la llamada a la API.

## Funcionalidades Principales

*   **Navegación**: La aplicación consta de tres pantallas principales:
    1.  `InitScreen`: Una portada inicial.
    2.  `StoreScreen`: Muestra la lista completa de productos obtenidos de la API.
    3.  `FavoritesScreen`: Muestra únicamente los productos que el usuario ha marcado como favoritos.
*   **Gestión de Favoritos**: La funcionalidad de favoritos persiste en la memoria de la aplicación mientras está en uso, compartiendo el estado entre las diferentes pantallas.

## Arquitectura General

La aplicación sigue los principios de **Arquitectura Limpia (Clean Architecture)** y el patrón **MVVM (Model-View-ViewModel)**. Esta arquitectura promueve una separación estricta de responsabilidades, lo que hace que el código sea más robusto, escalable y fácil de testear y mantener.

Las capas principales de la aplicación son:

1.  **Capa de UI (Interfaz de Usuario)**: Construida con **Jetpack Compose**, su única responsabilidad es renderizar el estado proporcionado por los ViewModels y capturar las interacciones del usuario.
2.  **Capa de ViewModel**: Actúa como un puente entre la UI y la capa de Datos. Contiene la lógica de presentación, gestiona el estado de la UI y sobrevive a cambios de configuración.
3.  **Capa de Datos (Data Layer)**: Responsable de la obtención y gestión de los datos, abstrayendo su origen.

## Estructura de Paquetes y Componentes

### 1. `com.fp.network` - Capa de Acceso a Red

Contiene toda la lógica de comunicación con la API.

-   **`ProductApiService` (Interfaz)**: Define los *endpoints* de la API utilizando **Retrofit**.
-   **`ApiException.kt` (Clase)**: Una excepción personalizada para encapsular errores de la API.

### 2. `com.fp.model` - Modelos de Datos

Contiene las clases de datos (`data class`) que representan la estructura de la respuesta JSON y los modelos para la UI.

-   **Modelos de Red (`Product.kt`, `ProductPage.kt`)**: Mapean directamente la respuesta de la API.
-   **Modelos de UI (`ProductWrapper.kt`, `ProductPageWrapper.kt`)**: "Envuelven" los modelos de red, añadiendo propiedades necesarias para la UI como `id` o `expanded`.

### 3. `com.fp.data.repository` - Repositorio e Inyección de Dependencias

Es el núcleo de la capa de datos y actúa como la **única fuente de verdad (Single Source of Truth)**.

-   **`ProductsRepository` (Interfaz)**: Define el contrato para obtener datos (`suspend fun getProducts()`).
-   **`NetworkProductsRepository` (Clase)**: Implementación concreta que llama a la API.
-   **`AppContainer.kt`**: Actúa como un **contenedor de dependencias manual**.

### 4. `com.fp.ui` - Capa de UI y ViewModels

Este paquete contiene toda la lógica de presentación, incluyendo la nueva gestión de favoritos.

-   **`ProductsViewModel`**: Responsable de la lógica de la pantalla `StoreScreen`. Obtiene la lista completa de productos del `ProductsRepository`.
-   **`FavoriteViewModel`**: **¡NUEVO!** Gestiona el estado de los productos favoritos.
    -   Expone un `StateFlow` con la lista de productos marcados como favoritos.
    -   Proporciona métodos para añadir, eliminar o alternar el estado de favorito de un producto (`toggleFavorite`).
    -   **Es un ViewModel compartido**: Su instancia es única y se comparte entre `StoreScreen` y `FavoritesScreen` para mantener un estado de favoritos consistente en toda la aplicación.
-   **Componentes de Compose (`StoreScreen.kt`, `FavoritesScreen.kt`, `ProductItem.kt`)**:
    -   `StoreScreen` y `FavoritesScreen` observan sus respectivos ViewModels para mostrar los datos.
    -   `ProductItem` ahora recibe un estado `isFavorite` y una función `onToggleFavorite` para permitir al usuario interactuar con el `FavoriteViewModel`.

## Inyección de Dependencias (DI) y Estado Compartido

La DI se implementa de forma manual a través de la clase `DefaultAppContainer`, que centraliza la creación de dependencias como `Retrofit` y `ProductsRepository`.

La gestión de favoritos introduce un concepto importante: **compartir un ViewModel**.

1.  **Creación del `FavoriteViewModel`**: Se crea una única instancia a nivel de la `MainActivity` o del `NavHost`.
2.  **Inyección en las Pantallas**: Esta única instancia del `FavoriteViewModel` se pasa como parámetro a las pantallas que la necesitan (`StoreScreen` y `FavoritesScreen`).
3.  **Consistencia de Estado**: Cuando un usuario marca un producto como favorito en `StoreScreen`, el `FavoriteViewModel` se actualiza. Como `FavoritesScreen` observa el mismo ViewModel, al navegar a ella, la lista de favoritos ya estará actualizada automáticamente, y viceversa.

