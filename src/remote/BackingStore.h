#pragma once

#include "../access/Access.h"
#include "../consistency/Consistency.h"

#define BSref_(name) name ## Cons, name ## Access, name ## Atype, name, name ##Obj

#define BackingStore_PAC(name, cc) name ## Cons extends consistency.Top, Access_C(name ## Access, access.Unknown & cc), name ## Atype, name extends remote.BackingStore<name ## Cons, name ## Access, name ## Atype, name, ?>, name ## Obj extends BackingStore<BSref_(name)>.RemoteObject

#define BackingStore_(name) BackingStore_PAC(name, access.Unknown)

#define BS_t_(name) remote.BackingStore<name ## Cons, name ## Access, name ## Atype, name, name ## Obj>

#define R_(t) t extends java.io.Serializable
#define R_g(t) t
