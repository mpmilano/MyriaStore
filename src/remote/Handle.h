#pragma once

#include "../remote/BackingStore.h"

#define Handle_P_(x) R_(x ## T), BackingStore_(x ## BS), Access_C(x ## A,x ## BSAccess), Consistency_C(x ## C,x ## BSCons)
#define Handle_PAC_(x,CC) R_(x ## T), BackingStore_(x ## BS), Access_C(x ## A,x ## BSAccess & CC), Consistency_C(x ## C,x ## BSCons)
#define Handle_P_g(x) R_g(x ## T), BSref_(x ## BS), x ## A, x ## C

#define Handle_fromBS(x) R_g(x ## T), BSref_(x ## BS), x ## BSAccess, x ## BSCons
