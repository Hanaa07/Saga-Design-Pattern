# Saga Design Pattern Implementation

## Overview

The **Saga Design Pattern** is a distributed transaction management pattern that divides a business process into smaller, independent steps (or transactions). It ensures consistency across services by coordinating compensating actions for failed transactions.

![image](https://github.com/user-attachments/assets/3b99dd49-1e96-411d-8f40-01d57ef487d2)

This implementation demonstrates two approaches:
1. **Orchestration**: A central orchestrator controls the sequence of transactions.
2. **Choreography**: Each service performs its role independently and communicates with others via events.

---

## Orchestration Model

### Key Classes and Components

1. **`Saga`**:
   - Represents the series of transactions in a saga.
   - **Attributes**:
     - `chapters`: A list of `Chapter` objects, representing the steps of the saga.
   - **Methods**:
     - `chapter(name)`: Adds a new chapter to the saga.
     - `create()`: Creates a new saga.
     - `get(idx)`: Retrieves a chapter by index.
     - `isPresent(idx)`: Checks if a chapter exists at the given index.

2. **`Chapter`**:
   - Represents a single step in the saga.
   - **Attributes**:
     - `name`: The name of the chapter.
     - `result`: The result of the chapter's execution.
   - **Methods**:
     - `getName()`: Returns the chapter name.
     - `setResult(result)`: Sets the result of the chapter.

3. **`SagaOrchestrator`**:
   - Central controller that manages the saga's execution flow.
   - **Attributes**:
     - `saga`: The saga being orchestrated.
     - `sd`: The `ServiceDiscoveryService` to find and use the required services.
     - `state`: Tracks the current state of the saga execution.
   - **Methods**:
     - `execute(value)`: Executes the saga steps in sequence, handling success and failure cases.

4. **`CurrentState`**:
   - Tracks the current position and direction (forward or backward) of the saga.
   - **Methods**:
     - `forward()`: Moves the saga to the next chapter.
     - `back()`: Moves the saga to the previous chapter.

5. **Services**:
   - Each service (e.g., `FlyBookingService`, `HotelBookingService`, `OrderService`) implements `OrchestrationChapter`.
   - **Methods**:
     - `process(value)`: Executes the main logic for the chapter.
     - `rollback(value)`: Executes compensating logic in case of failure.

6. **`ServiceDiscoveryService`**:
   - Responsible for locating services involved in the saga.
   - **Methods**:
     - `discover(service)`: Registers a service for discovery.
     - `find(service)`: Retrieves a registered service by name.

---

## Choreography Model

### Key Classes and Components

1. **`Saga`**:
   - Represents the saga, similar to the orchestration model, but with additional fields for event-driven communication.
   - **Attributes**:
     - `chapters`: The steps of the saga.
     - `finished`: Indicates whether the saga is complete.
     - `forward`: Direction of execution (forward or rollback).
   - **Methods**:
     - `setInValue(value)`: Sets input data for the saga.
     - `getResult()`: Retrieves the final result of the saga.

2. **`ChoreographyChapter`**:
   - Interface defining steps in a choreography.
   - **Methods**:
     - `process(Saga)`: Executes the main logic for the chapter.
     - `rollback(Saga)`: Executes compensating actions in case of failure.
     - `execute(Saga)`: Handles chapter execution, including success and failure cases.

3. **Services**:
   - Similar to orchestration, services such as `FlyBookingService`, `HotelBookingService`, and `OrderService` participate in the saga.
   - In the choreography model, they use event-driven communication to trigger their actions.

4. **`ServiceDiscoveryService`**:
   - Provides a mechanism to discover and communicate with other services in the choreography.

---

## Key Enums

1. **`State`**:
   - Represents the state of a chapter in the saga.
   - Values: `SUCCESS`, `FAILURE`.

2. **`Result`** (Orchestration):
   - Represents the outcome of a saga execution.
   - Values: `FINISHED`, `ROLLBACK`, `CRASHED`.

3. **`ChapterResult`** (Choreography):
   - Represents the outcome of a chapter in the saga.
   - Values: `SUCCESS`, `ROLLBACK`.

4. **`SagaResult`** (Choreography):
   - Represents the final state of a saga.
   - Values: `FINISHED`, `ROLLBACKED`, `PROGRESS`.

---

## How It Works

### Orchestration Example

1. **Saga Initialization**:
   - A `Saga` is created with chapters representing each transaction (e.g., book a flight, reserve a hotel, place an order).
2. **Execution**:
   - `SagaOrchestrator` executes each chapter in sequence. If a chapter fails, it rolls back previously executed chapters.
3. **Service Interaction**:
   - Each service processes the current step and returns a `ChapterResult` (`SUCCESS` or `FAILURE`).

### Choreography Example

1. **Saga Initialization**:
   - A `Saga` is created, and services register themselves in `ServiceDiscoveryService`.
2. **Execution**:
   - Each service listens for events and performs its operation when triggered, returning a result.
3. **Event-Driven Rollback**:
   - If a failure occurs, services trigger rollback events for compensating actions.

---

## Benefits of Saga Pattern

1. **Resiliency**:
   - Handles partial failures with compensating transactions, ensuring eventual consistency.
2. **Decoupling**:
   - Services remain independent in the choreography model.
3. **Flexibility**:
   - Orchestration provides centralized control, while choreography enables decentralized communication.
