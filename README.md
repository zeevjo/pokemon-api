# Pokemon API

A simple REST API built with Spring Boot and Kotlin that provides information about the original 151 Pokemons.

## Features

- List all available Pokemons
- Retrieve detailed information about a specific Pokemon by ID
- Data includes name, types, PokedexNumber, and image URL for each Pokemon

## Tech Stack

- Kotlin
- Spring Boot

## Getting Started

### Clone the repository

```bash
  git clone https://github.com/zeevjo/pokemon-api.git
  cd pokemon-api
```

### Build the application

```bash
  ./gradlew clean build
```

### Run the application

```bash
  ./gradlew bootRun
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Get all Pokemons

```
GET /api/pokemon
```

Example response:
```json
[
  {
    "id": 1,
    "name": "Bulbasaur",
    "types": ["Grass", "Poison"],
    "pokedexNumber": "001",
    "imageUrl": "http://www.serebii.net/pokemongo/pokemon/001.png"
  },
  {
    "id": 2,
    "name": "Ivysaur",
    "types": ["Grass", "Poison"],
    "pokedexNumber": "002",
    "imageUrl": "http://www.serebii.net/pokemongo/pokemon/002.png"
  },
  // ... more Pokemons
]
```

### Get Pokemon by ID

```
GET /api/pokemon/{id}
```

Example response for `/api/pokemon/25`:
```json
{
  "id": 25,
  "name": "Pikachu",
  "types": ["Electric"],
  "pokedexNumber": "025",
  "imageUrl": "http://www.serebii.net/pokemongo/pokemon/025.png"
}
```

## Running Tests

To run the tests:

```bash
    ./gradlew test
```

For more documentation, please refer to [help.md](help.md).