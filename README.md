# PM Products API - Rama MVI

Este documento detalla la arquitectura técnica de la rama `MVI` del proyecto `pm_products_api`. La aplicación ha sido refactorizada para seguir un patrón de diseño **MVI (Model-View-Intent)** sobre la base de una **Arquitectura Limpia (Clean Architecture)**.

El objetivo de esta arquitectura es conseguir un flujo de datos estrictamente unidireccional y un manejo de estado predecible, haciendo la aplicación aún más robusta, depurable y fácil de testear.

## Arquitectura General

La aplicación sigue los principios de **Arquitectura Limpia** con una implementación de **MVI** en la capa de presentación (UI y ViewModel).

1.  **Capa de UI (View)**: Construida con **Jetpack Compose**. Es una representación pasiva del estado y se encarga de capturar las intenciones (acciones) del usuario.
2.  **Capa de `Store` (ViewModel)**: Actúa como el núcleo de la lógica de presentación. Orquesta el flujo de datos, procesa las acciones, actualiza el estado y gestiona los efectos secundarios (como las llamadas a la API).
3.  **Capa de Datos (Data Layer)**: Responsable de obtener y gestionar los datos, abstrayendo su origen.

## Estructura de Paquetes y Componentes

### 1. `com.fp.network` - Capa de Acceso a Red

Este paquete contiene toda la lógica relacionada con la comunicación de red. Es la capa más profunda y no tiene conocimiento de ninguna otra parte de la aplicación.

-   **`ProductApiService` (Interfaz)**: Define los *endpoints* de la API utilizando **Retrofit**. Especifica las operaciones de red (`@GET`, etc.) y los tipos de datos que se esperan.
-   **`ApiException.kt` (Clase)**: Una excepción personalizada que encapsula errores de la API (ej: códigos de estado 4xx o 5xx).

### 2. `com.fp.model` - Modelos de Datos

Este paquete, ahora independiente, contiene todas las clases de datos (`data class`) que representan entidades de la aplicación, promoviendo una mejor separación de responsabilidades.

-   **Modelos de Red (`Product.kt`, `ProductPage.kt`)**: Clases anotadas con `@Serializable` que mapean directamente la respuesta JSON de la API.
-   **Modelos de UI (`ProductWrapper.kt`, `ProductPageWrapper.kt`)**: Modelos que "envuelven" a los de red. Contienen datos adicionales necesarios exclusivamente para la presentación en la UI (`id`, `expanded`, etc.), desacoplando la vista de la estructura de la API.

### 3. `com.fp.data.repository` - Repositorio y Contenedor de Dependencias

Este paquete es el núcleo de la capa de datos y actúa como la **única fuente de verdad (Single Source of Truth)**.

-   **`ProductsRepository` (Interfaz)**: Define el contrato que la capa de datos debe cumplir.
-   **`NetworkProductsRepository` (Clase)**: Implementación concreta que llama a la API, maneja errores y transforma los modelos de red en modelos de UI.
-   **`AppContainer.kt`**: Actúa como un **contenedor de dependencias manual**.

### 4. `com.fp.ui` - Capa de UI

Este paquete contiene la implementación de la vista y la gestión del estado MVI.

-   **Sub-paquete `store`**: Contiene los componentes clave del patrón MVI.
    -   **`ActionEnum.kt` (`Action`)**: Las intenciones o acciones del usuario/sistema.
    -   **`State.kt` (`State`)**: La estructura de datos inmutable que representa el estado completo de la UI.
    -   **`Reducer.kt` (`Reducer`)**: Función pura que calcula el nuevo estado a partir del estado actual y una acción.
    -   **`Store.kt` (`Store`/ViewModel)**: Orquesta todo el flujo MVI.
-   **`ProductsScreen.kt`**: El componente Composable que observa el estado del `Store` y le despacha acciones.

## Inyección de Dependencias (DI) con MVI

La Inyección de Dependencias se implementa de forma manual a través de la clase `DefaultAppContainer`:

1.  **Creación Centralizada**: `DefaultAppContainer` es el único que sabe cómo construir los objetos complejos (`Retrofit`, `ProductApiService`, `NetworkProductsRepository`).
2.  **Inyección en el `Store`**: El `Store` (que actúa como ViewModel) recibe el `ProductsRepository` a través de su constructor. Para ello, se utiliza una `ViewModelProvider.Factory` que obtiene la instancia del repositorio desde el `AppContainer`.

**Ventajas de este enfoque:**

-   **Testeabilidad**: Permite inyectar dependencias falsas (`FakeRepository`) en los tests.
-   **Flexibilidad**: Facilita el cambio de implementaciones (ej. pasar de una API a una base de datos local) sin afectar al resto de la app.

## Flujo de Datos Unidireccional en MVI

El flujo de datos es un ciclo cerrado y predecible:

![MVI Data Flow](https://miro.medium.com/v2/resize:fit:1400/1*5A9s1hp34n2a5dF22a_72g.png)

1.  **Acción (Intent)**: La **UI** despacha una `Action` al `Store` (ej: `ActionEnum.READ`).
2.  **Procesamiento (`Store`)**: El `Store` identifica la acción. Si requiere un **efecto secundario** (como una llamada a la API), primero despacha una acción para reflejar un estado intermedio (ej: `IS_LOADING`).
3.  **Efecto Secundario (`Store`)**: El `Store` ejecuta la operación asíncrona (llama al `Repository`). Al completarse, despacha una nueva acción con el resultado (`READ` con los datos, o `ERROR` con una excepción).
4.  **Reducción (`Reducer`)**: El `Reducer` recibe la acción y el estado actual, y devuelve un **nuevo estado inmutable**.
5.  **Renderizado (`View`)**: El `Store` emite el nuevo estado a través de su `StateFlow`. La **UI** lo recibe, se recompone y muestra el resultado final.

El ciclo se completa y la aplicación queda a la espera de una nueva acción.
