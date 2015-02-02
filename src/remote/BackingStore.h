#pragma once

#include "../access/Access.h"
#include "../consistency/Consistency.h"

#define BSref_(name) name ## Cons, name ## Atype, name, name ##Obj

#define BackingStore_PAC(name, cc, bsc) name ## Cons extends consistency.Top, name ## Atype, name, name ## Obj extends BackingStore<BSref_(name)>.RemoteObject

#define BackingStore_PC(name, cc) BackingStore_PAC(name, cc, util.Dummy)

#define BackingStore_(name) BackingStore_PAC(name, access.Unknown, util.Dummy)

#define BS_t_(name) remote.BackingStore<name ## Cons, name ## Atype, name, name ## Obj>


#define R_(t) t extends java.io.Serializable
#define R_g(t) t
