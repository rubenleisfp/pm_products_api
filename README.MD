# PM Products API - Descripción Técnica

En esta rama se llama al API dummyjson/products [https://dummyjson.com/docs/products](https://dummyjson.com/docs/products).

Hay capa de repositorio e inyeccion de dependencias.

Existe una pequeña navegacion, donde la aplicacion muestra una ventana inicial con una portada.

Al pulsar sobre el boton de la portada, navegaremos a la ventana StoreScree, donde se muestra la lista de productos.
Desde la lista de productos podemos volver a la portada pulsando sobre el boton del TopBar.

Este documento detalla la arquitectura técnica y el funcionamiento interno de la aplicación `pm_products_api`. 
La app está diseñada para obtener y mostrar una lista de productos desde una API pública (`https://dummyjson.com/`), siguiendo las directrices de arquitectura moderna de Android recomendadas por Google.

## Arquitectura General

La aplicación sigue los principios de **Arquitectura Limpia (Clean Architecture)** y el patrón **MVVM (Model-View-ViewModel)**. Esta arquitectura promueve una separación estricta de responsabilidades, lo que hace que el código sea más robusto, escalable, y fácil de testear y mantener.

Las capas principales de la aplicación son:

1.  **Capa de UI (Interfaz de Usuario)**: Construida con **Jetpack Compose**, su única responsabilidad es renderizar el estado proporcionado por el ViewModel y capturar las interacciones del usuario.
2.  **Capa de ViewModel**: Actúa como un puente entre la UI y la capa de Datos. Contiene la lógica de presentación, gestiona el estado de la UI y sobrevive a cambios de configuración.
3.  **Capa de Datos (Data Layer)**: Responsable de la obtención y gestión de los datos, abstrayendo su origen (en este caso, una API REST). Es la única capa que sabe *cómo* se obtienen los datos.

A continuación, se detalla la estructura de paquetes y la interacción entre los componentes.

## Estructura de Paquetes y Componentes

### 1. `com.fp.network` - Capa de Acceso a Red

Este paquete contiene toda la lógica relacionada con la comunicación de red. Es la capa más profunda y no tiene conocimiento de ninguna otra parte de la aplicación.

-   **`ProductApiService` (Interfaz)**: Define los *endpoints* de la API utilizando **Retrofit**. Especifica las operaciones de red (`@GET`, etc.) y los tipos de datos que se esperan.
-   **`ApiException.kt` (Clase)**: Una excepción personalizada que encapsula errores de la API (ej: códigos de estado 4xx o 5xx). Almacena información clave como el código y el mensaje de error, permitiendo un manejo de errores más elegante y estructurado.

### 2. `com.fp.model` - Modelos de Datos

Este paquete contiene las clases de datos (`data class`) que representan la estructura de la respuesta JSON de la API.

-   **`ProductPage.kt`, `Product.kt`, etc.**: Son clases simples anotadas con `@Serializable` para que la librería **Kotlinx Serialization** pueda convertirlas automáticamente de JSON a objetos Kotlin.
-   **`ProductPageWrapper.kt`, `ProductWrapper.kt`**: Modelos de datos específicos para la UI que "envuelven" los modelos de red. Añaden propiedades adicionales necesarias para la lógica de la interfaz (como `id` o `expanded`), desacoplando la UI de la estructura exacta de la API.

### 3. `com.fp.data.repository` - Repositorio y Contenedor de Dependencias

Este paquete es el núcleo de la capa de datos y actúa como la **única fuente de verdad (Single Source of Truth)**. También es donde se gestiona la Inyección de Dependencias.

-   **`ProductsRepository` (Interfaz)**: Define el contrato que la capa de datos debe cumplir (`suspend fun getProducts(): ProductPageWrapper`). Los ViewModels dependen de esta **abstracción**, no de su implementación concreta.
-   **`NetworkProductsRepository` (Clase)**: Es la implementación concreta de `ProductsRepository`. Su responsabilidad es llamar a la API, manejar la respuesta, lanzar excepciones si es necesario y transformar los datos para las capas superiores.
-   **`AppContainer.kt` (Interfaz y Clase)**: Actúa como un **contenedor de dependencias manual**. Esta es la pieza central de la Inyección de Dependencias en el proyecto.

### 4. `com.fp.ui` - Capa de UI y ViewModel

Este paquete contiene toda la lógica de presentación.

-   **`ProductsViewModel` (Clase)**: Contiene la lógica de negocio de la pantalla. Recibe sus dependencias (el `ProductsRepository`) a través de su constructor, expone el estado de la UI (`UiState`) mediante un `StateFlow` y utiliza `viewModelScope` para ejecutar operaciones de larga duración de forma segura.
-   **Componentes de Compose (`ProductsScreen.kt`, etc.)**: Funciones Composable "tontas" que se limitan a observar el `UiState` del ViewModel y a notificarle los eventos del usuario.

## Inyección de Dependencias (DI) - ¿Cómo se Aplica?

La Inyección de Dependencias es una técnica clave en esta arquitectura. En lugar de que un objeto cree sus propias dependencias (ej. `ViewModel` creando su `Repository`), estas se le "inyectan" desde fuera. Esto reduce el acoplamiento y mejora drásticamente la testeabilidad.

En este proyecto, se implementa una **Inyección de Dependencias Manual** a través de la clase `DefaultAppContainer`:

1.  **Creación Centralizada de Dependencias**: La clase `DefaultAppContainer` es la única que sabe cómo construir los objetos complejos y sus dependencias.
    -   Crea una instancia de `Retrofit`.
    -   Usa esa instancia para crear el `ProductApiService`.
    -   Finalmente, crea la instancia del `NetworkProductsRepository`, **inyectándole** en su constructor el `ProductApiService` que acaba de crear.

    