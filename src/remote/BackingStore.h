#pragma once

#define BackingStore_(name) BSCons extends consistency.Top, name extends BackingStore<BSCons, name> 
