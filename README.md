# Project Readme

## Project Overview

This project implements the use of `PreferenceDataStore` in Android to efficiently handle and manage user preferences.
<br/><br/> **What is user Preferences?** 
<br/> user preferences a set of personal data that is of very small size and doesnot require a complete DataBase service to be stored, like wether app is getting opened first time so that we can show OnBoarding Screen, Access Tokens for a specipic server etc. this service is provided by`PreferenceDataStore` 
<br/><br/> `PreferenceDataStore` is part of the Android DataStore library, which provides a modern, coroutine-based API for storing simple data asynchronously.
<p align="center">
    <img src="https://github.com/AngryFalcon89/PreferenceDataStore/assets/91687355/5cd3f4d5-bac3-4dd2-93c2-34873887aff1" width="200" alt="Screenshot 1">
</p>

## Purpose of PreferenceDataStore

The primary purpose of using `PreferenceDataStore` is to provide a more robust, consistent, and coroutine-friendly alternative to the traditional `SharedPreferences`. It addresses some of the limitations of `SharedPreferences` and is particularly well-suited for handling asynchronous operations and complex data storage needs.

Key benefits of using `PreferenceDataStore` include:

- **Coroutines Support:** It seamlessly integrates with Kotlin coroutines, making it easier to handle asynchronous operations without blocking the main thread.
  
- **Type-Safety:** It allows you to store and retrieve strongly-typed data, reducing the risk of runtime errors associated with using keys to access values.
  
- **DataStore:** It is built on top of the DataStore library, which is designed to handle more complex data scenarios and provides better support for data consistency and synchronization.

## Implementation Steps

### 1. Add the Dependency

```gradle
implementation "androidx.datastore:datastore-preferences:1.0.0"
```
### 2. Create a Preference DataStore
Create a PreferenceDataStore instance using the PreferenceDataStoreFactory. This is typically done in your Application class or a Dagger Hilt module.

```kotlin
val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
    context.preferencesDataStoreFile("user_data")
}
```
### 3. Define Preference Keys
Define keys for your preferences using the preferencesKey function. This provides a type-safe way to access preferences.

```kotlin
val USER_NAME_KEY = stringPreferencesKey("user_name")
```
### 4. Create an Interface for User Preferences
Define an interface that represents the user preferences and the operations you want to perform.

```kotlin
interface UserPreferences {

    val userName: Flow<String>

    suspend fun saveUserName(name: String)

}
```

### 5. Implement the User Preferences Interface
Create an implementation of the interface using PreferenceDataStore.

```kotlin
class UserPreferencesImpl(private val dataStore: DataStore<Preferences>) : UserPreferences {

    override val userName: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_NAME_KEY] ?: ""
        }

    override suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

}
```

### 6. Dagger Hilt Integration (Optional)
If you're using Dagger Hilt, provide instances of DataStore<Preferences> and UserPreferences in your Dagger module.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_data")
        }
    }

    @Provides
    fun provideUserPreferences(dataStore: DataStore<Preferences>): UserPreferences {
        return UserPreferencesImpl(dataStore)
    }

}
```

### 7. Use User Preferences in ViewModel or Repository
Inject UserPreferences wherever you need to read or write user preferences. For example, in a ViewModel:

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(private val userPreferences: UserPreferences) : ViewModel() {

    val userName: Flow<String> = userPreferences.userName

    fun saveUserName(name: String) {
        viewModelScope.launch {
            userPreferences.saveUserName(name)
        }
    }

}
```
## Functionality
With this implementation, you can seamlessly handle user preferences with coroutines and benefit from a more structured, type-safe approach to storing and retrieving data. The UserPreferences interface abstracts away the underlying implementation details, providing a clean API for the rest of your application to interact with user preferences. The PreferenceDataStore ensures efficient and asynchronous handling of the stored data.
