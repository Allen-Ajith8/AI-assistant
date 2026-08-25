import os
import re

# 1. Update SharedViewModel to hold all states
svm_path = "app/src/main/java/com/example/gigshield/ui/SharedViewModel.kt"
with open(svm_path, "r", encoding="utf-8") as f:
    svm_content = f.read()

# Add more states
new_states = """
    val activePolicy = kotlinx.coroutines.flow.MutableStateFlow(com.example.gigshield.data.model.InsuranceTier.INCOME_PROTECTOR)
    val safeRiderScore = kotlinx.coroutines.flow.MutableStateFlow(92)
    val earningsToday = kotlinx.coroutines.flow.MutableStateFlow("₹1,250")
    val isWorking = kotlinx.coroutines.flow.MutableStateFlow(false)
"""

if "activePolicy" not in svm_content:
    svm_content = svm_content.replace('    fun startWork() {', new_states + '\n    fun startWork() {\n        isWorking.value = true\n')
    svm_content = svm_content.replace('fun submitClaim() {', 'fun submitClaim() {\n')
    with open(svm_path, "w", encoding="utf-8") as f:
        f.write(svm_content)

# 2. Update StartWorkScreen to use ViewModel
sws_path = "app/src/main/java/com/example/gigshield/ui/work/StartWorkScreen.kt"
with open(sws_path, "r", encoding="utf-8") as f:
    sws_content = f.read()

if "SharedViewModel" not in sws_content:
    sws_content = sws_content.replace('import androidx.compose.runtime.Composable', 'import androidx.compose.runtime.*\nimport com.example.gigshield.ui.SharedViewModel')
    sws_content = sws_content.replace('fun StartWorkScreen(onBack: () -> Unit)', 'fun StartWorkScreen(viewModel: SharedViewModel, onBack: () -> Unit)')
    sws_content = sws_content.replace('var isStarting by remember { mutableStateOf(false) }', 'val isWorking by viewModel.isWorking.collectAsState()\n    var isStarting by remember { mutableStateOf(false) }')
    sws_content = sws_content.replace('isStarting = true', 'viewModel.startWork(); isStarting = true')
    sws_content = sws_content.replace('Text("START WORK"', 'Text(if (isWorking) "WORK VERIFIED & ACTIVE" else "START WORK"')
    with open(sws_path, "w", encoding="utf-8") as f:
        f.write(sws_content)

# 3. Update MyClaimsScreen to use ViewModel
mcs_path = "app/src/main/java/com/example/gigshield/ui/claims/MyClaimsScreen.kt"
with open(mcs_path, "r", encoding="utf-8") as f:
    mcs_content = f.read()

if "SharedViewModel" not in mcs_content:
    mcs_content = mcs_content.replace('import androidx.compose.runtime.Composable', 'import androidx.compose.runtime.*\nimport com.example.gigshield.ui.SharedViewModel')
    mcs_content = mcs_content.replace('fun MyClaimsScreen(onBack: () -> Unit)', 'fun MyClaimsScreen(viewModel: SharedViewModel, onBack: () -> Unit)')
    mcs_content = re.sub(r'items\(3\).*?ClaimCard.*?\}', '''val claims by viewModel.claims.collectAsState()
            items(claims.size) { index ->
                val claim = claims[index]
                ClaimCard(
                    claimId = claim.id,
                    date = claim.date,
                    type = claim.type,
                    amount = claim.amount,
                    status = claim.status
                )
            }''', mcs_content, flags=re.DOTALL)
    with open(mcs_path, "w", encoding="utf-8") as f:
        f.write(mcs_content)

# 4. Update Navigation.kt to pass ViewModel to the new screens
nav_path = "app/src/main/java/com/example/gigshield/Navigation.kt"
with open(nav_path, "r", encoding="utf-8") as f:
    nav_content = f.read()

nav_content = nav_content.replace('StartWorkScreen(onBack = { backStack.removeLastOrNull() })', 'StartWorkScreen(viewModel = sharedViewModel, onBack = { backStack.removeLastOrNull() })')
nav_content = nav_content.replace('MyClaimsScreen(onBack = { backStack.removeLastOrNull() })', 'MyClaimsScreen(viewModel = sharedViewModel, onBack = { backStack.removeLastOrNull() })')

with open(nav_path, "w", encoding="utf-8") as f:
    f.write(nav_content)

print("UI wired to backend successfully.")
