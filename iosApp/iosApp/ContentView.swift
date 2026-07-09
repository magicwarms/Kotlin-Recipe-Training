import SwiftUI
import SharedLogic

struct ContentView: View {
    @StateObject private var viewModel = ViewModel()

    var body: some View {
        VStack(alignment: .center) {
            switch onEnum(of: viewModel.uiState) {
            case .loading:
                    ProgressView()
                    Text("Loading...")
            case .error(let error):
                    Text("Error: \(error.message)")
            case .success(let success):
                ForEach(success.data, id: \.id) { meal in
                    HStack {
                        // Image Later

                        VStack {
                            Text(meal.name)
                            Text(meal.country)
                        }
                        .padding(.horizontal, 16)
                        .padding(.vertical, 8)
                    }
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

extension ContentView {
    @MainActor
    class ViewModel: ObservableObject {
        let viewModel: MealViewModel

        @Published var uiState: UiState
        private var observationTask: Task<Void, Never>?

        init() {
            self.viewModel = MealViewModel()
            self.uiState = viewModel.uiState.value
            startObserving()
        }

        func startObserving() {
            observationTask = Task { [weak self] in
                guard let flow = self?.viewModel.uiState else {
                    return
                }
                for await state in flow {
                    guard let self else { break }
                    self.uiState = state
                }
            }
        }

        deinit {
            observationTask?.cancel()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
