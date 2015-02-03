#pragma once

#include "../remote/BackingStore.h"

#define Handle_Post_(x) BackingStore_(x ## BS), Access_(x ## A), Consistency_(x ## C)
#define Handle_P_(x) R_(x ## T), Handle_Post_(x)
#define Handle_PAC_(x,CC,BSC) R_(x ## T), BackingStore_PAC(x ## BS, CC, BSC), Access_(x ## A), Consistency_(x ## C)
#define Handle_fromBS2(x,y) R_g(x ## T), BSref2_(x ## BS,y ## BS), access.ReadWrite, x ## BSCons
#define Handle_fromBS(x) Handle_fromBS2(x,x)

#define Handle_Pre_g(x) R_g(x ## T), BSref_(x ## BS)
#define Handle_P_g(x) Handle_Pre_(x), x ## A, x ## C
#define Handle_Post_g(x) BSref_(x ## BS), x ## A, x ## C

#define CMA ,
