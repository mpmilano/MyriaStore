#pragma once

#include "../remote/BackingStore.h"

#define FSSp_ consistency.Lin, String, FSStore
#define FSS_ FSSp_, FSStore.FSObject
#define FSS_t BackingStore<FSS_>
