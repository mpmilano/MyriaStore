#pragma once

#include "../access/Access.h"
#include "../consistency/Consistency.h"

#define BSref2_(name,y) name ## Cons, name ## Atype, name, y ##Obj
#define BSref_(name) BSref2_(name,name)

#define BackingStore_PAC(name, cc, bsc) Consistency_(name ## Cons), name ## Atype, name, name ## Obj extends BackingStore<name ## Cons,name ## Atype,name,?>.RemoteObject

#define BackingStore_PC(name, cc) BackingStore_PAC(name, cc, util.Dummy)

#define BackingStore_(name) BackingStore_PAC(name, access.Unknown, util.Dummy)

#define BS_t_(name) remote.BackingStore<name ## Cons, name ## Atype, name, name ## Obj>

#define BSref_min(name) name ## Cons, ?, name, name ## Obj
#define BackingStore_min(name) Consistency_(name ## Cons), name, name ## Obj extends BackingStore<BSref_min(name)>.RemoteObject


#define R_(t) t extends java.io.Serializable
#define R_g(t) t


#define OpBasics(x) S extends operations.Get<HBSObj> & operations.Put<HBSObj>, HBSObj, Consistency_(C), H extends x & remote.StoreActions<S,HBSObj>
#define OpBasics_g S, HBSObj, C, H
