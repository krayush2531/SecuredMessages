**Project Title:** SecuredMessages

**Goal**
Create an open-source Android application that automatically classifies incoming SMS messages based on the Indian DLT sender ID category suffix (e.g., `-P`, `-S`, `-G`, `-T`). The app should parse the sender ID and organize messages into categories such as Promotional, Service, Government, and Transactional.

---

## Background

In India, SMS headers follow a format like:

```
VA-MYCLQ-P
JD-SBIUPD-S
VK-UIDAI-G
```

Structure:

```
OP-ENTITY-CATEGORY
```

Where:

* **OP** → Telecom operator code (2 characters)
* **ENTITY** → Business or organization identifier
* **CATEGORY** → Message category

Category meanings:

| Letter | Meaning            |
| ------ | ------------------ |
| P      | Promotional        |
| S      | Service            |
| G      | Government         |
| T      | Transactional      |
| I      | Implicit           |
| U      | Spam / Unsolicited |

The app must detect the **last segment after the final dash (`-`)** and classify the message accordingly.

---

# Functional Requirements

## 1. SMS Reception

The app must listen for incoming SMS using an Android `BroadcastReceiver`.

When an SMS is received:

1. Extract the sender ID
2. Parse the category suffix
3. Assign the message to the appropriate category

Example logic:

```
sender = "VA-MYCLQ-P"
category = sender.split("-").last()
```

---

## 2. SMS Categorization

Map the category code:

```
P → Promotional
S → Service
G → Government
T → Transactional
I → Implicit
U → Spam
```

If the sender ID does not match the format, classify as:

```
Others
```

---

## 3. Message Storage

Use a local database (Room / SQLite).

Schema example:

Messages table:

* id
* sender
* body
* timestamp
* category

---

## 4. User Interface

Build a simple UI with tabs:

Home screen tabs:

* All
* Promotional
* Service
* Government
* Transactions
* Spam

Each tab shows messages belonging to that category.

Use:

* RecyclerView
* Material Design components

---

## 5. Filtering Rules

Rules should include:

```
sender endsWith "-P" → Promotional
sender endsWith "-S" → Service
sender endsWith "-G" → Government
sender endsWith "-T" → Transactional
sender endsWith "-I" → Implicit
sender endsWith "-U" → Spam
```

Use regex if necessary.

Example:

```
.*-P$
```

---

# Technical Requirements

Language: Kotlin
Minimum SDK: 26
Architecture: MVVM

Use:

* Jetpack Compose OR XML layouts
* Room Database
* ViewModel
* LiveData / Flow

---

# Permissions

The app requires:

```
RECEIVE_SMS
READ_SMS
```

Handle runtime permissions properly.

---

# Additional Features (Optional but Recommended)

1. OTP Detection
   Automatically highlight messages containing OTP.

2. Spam Blocking
   Allow the user to mute or hide promotional messages.

3. Notification Categorization
   Show notifications grouped by category.

4. Statistics
   Display daily stats:

* promotional messages received
* service alerts
* spam count

---

# Edge Cases

Handle cases where:

* Sender is a normal phone number
* Sender does not contain "-"
* Category letter missing
* Unknown category letter

Fallback category:

```
Unknown
```

---

# Project Structure

Suggested structure:

```
app
 ├── data
 │    ├── database
 │    └── repository
 ├── sms
 │    └── SmsReceiver
 ├── parser
 │    └── CategoryParser
 ├── ui
 │    ├── screens
 │    └── components
 ├── viewmodel
 └── MainActivity
```
