#pragma once

#include "../access/Access.h"
#include "../consistency/Consistency.h"

#define BackingStore_(name) name ## Cons extends consistency.Top, Access_(name ## Access), name ## Atype, name extends remote.BackingStore<name ## Cons, name ## Access, name ## Atype, name> 
#define BSref_(name) name ## Cons, name ## Access, name ## Atype, name
#define BS_t_(name) remote.BackingStore<name ## Cons, name ## Access, name ## Atype, name>

#define R_(t) t extends java.io.Serializable
#define R_g(t) t
