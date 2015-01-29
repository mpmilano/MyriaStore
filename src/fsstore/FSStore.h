#pragma once

#include "../remote/BackingStore.h"

#define FSS_(T) consistency.Lin, access.ReadWrite, String, FSStore<T>
#define FSS_t(T) BackingStore<FSS_(T)>
