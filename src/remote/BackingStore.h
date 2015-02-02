#pragma once

#include "../access/Access.h"
#include "../consistency/Consistency.h"

#define BSref_(name) name ## Cons, name ## Access, name ## Atype, name, name ##Obj

#define BackingStore_PAC(name, cc, bsc) name ## Cons extends consistency.Top, Access_C(name ## Access, access.Unknown & cc), name ## Atype, name extends remote.BackingStore<name ## Cons, name ## Access, name ## Atype, name, ?> & bsc, name ## Obj extends BackingStore<BSref_(name)>.RemoteObject

#define BackingStore_(name) BackingStore_PAC(name, access.Unknown, util.Dummy)

#define BS_t_(name) remote.BackingStore<name ## Cons, name ## Access, name ## Atype, name, name ## Obj>

#define someBackingStore_(name) name extends remote.BackingStore<?,? ,? , HBS, ?>
#define someStoreObj_(name) HBSObj extends BackingStore<?, ?, ?, HBS, HBSObj>.RemoteObject

#define R_(t) t extends java.io.Serializable
#define R_g(t) t
