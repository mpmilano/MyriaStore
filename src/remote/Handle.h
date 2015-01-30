#pragma once

#include "../remote/BackingStore.h"

#define Handle_P_(x) R_(x ## T), BackingStore_(x ## BS), x ## BSObj extends BackingStore<BSref_(x ## BS)>.RemoteObject, Access_C(x ## A,x ## BSAccess), Consistency_C(x ## C,x ## BSCons)
#define Handle_PAC_(x,CC,BSC) R_(x ## T), BackingStore_PAC(x ## BS, CC) & BSC, x ## BSObj extends BackingStore<BSref_(x ## BS)>.RemoteObject, Access_C(x ## A,x ## BSAccess), Consistency_C(x ## C,x ## BSCons)

#define Handle_P_g(x) R_g(x ## T), BSref_(x ## BS), x ## BSObj, x ## A, x ## C

#define CMA ,
