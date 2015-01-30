#pragma once

#include "../remote/BackingStore.h"

#define FSS_ consistency.Lin, access.ReadWrite, String, FSStore
#define FSS_t BackingStore<FSS_>
