//
//  TimePicker.swift
//  iosApp
//
//  Created by Trevor Wiebe on 4/29/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

struct TimePicker: View{
    
    var punch: Punch
    var onDismiss: () -> Void
    var onTimeSelected: (Punch) -> Void
    @State private var selectedDate: Date
    
    struct TimePickerRepresentable: UIViewRepresentable {
        @Binding var selectedDate: Date

        func makeUIView(context: Context) -> UIDatePicker {
            let picker = UIDatePicker()
            picker.datePickerMode = .time
            picker.preferredDatePickerStyle = .wheels
            picker.isUserInteractionEnabled = true
            picker.addTarget(context.coordinator, action: #selector(Coordinator.dateChanged(_:)), for: .valueChanged)
            return picker
        }

        func updateUIView(_ uiView: UIDatePicker, context: Context) {
            uiView.date = selectedDate
        }

        func makeCoordinator() -> Coordinator {
            Coordinator(self)
        }

        class Coordinator: NSObject {
            var parent: TimePickerRepresentable

            init(_ parent: TimePickerRepresentable) {
                self.parent = parent
            }

            @objc func dateChanged(_ sender: UIDatePicker) {
                parent.selectedDate = sender.date
            }
        }
    }
            
    init(punch: Punch, onDismiss: @escaping () -> Void, onTimeSelected: @escaping (Punch) -> Void) {
        self.punch = punch
        self.onDismiss = onDismiss
        self.onTimeSelected = onTimeSelected
        let components = NSDateComponents()
        components.hour = Int(punch.dateTime.hour)
        components.minute = Int(punch.dateTime.minute)
        let calendar = Calendar.current
        self._selectedDate = State(initialValue: calendar.date(from: components as DateComponents) ?? Date())
    }
    
    var body: some View {
        ZStack {
            // Dimmed background
            Color.black.opacity(0.4)
                .edgesIgnoringSafeArea(.all)
                .onTapGesture {
                    onDismiss()
                }

            // Dialog card
            VStack(spacing: 0) {
                TimePickerRepresentable(selectedDate: $selectedDate)
                    .frame(height: 200)

                HStack {
                    Button(action: onDismiss) {
                        Text("Cancel")
                            .foregroundColor(.red)
                            .frame(maxWidth: .infinity)
                    }

                    Button(action: {
                        let calendar = Calendar.current
                        let hour = calendar.component(.hour, from: selectedDate)
                        let minute = calendar.component(.minute, from: selectedDate)
                        let updatedDateTime = Kotlinx_datetimeLocalDateTime(
                            date: punch.dateTime.date,
                            time: Kotlinx_datetimeLocalTime(
                                hour: Int32(hour),
                                minute: Int32(minute),
                                second: 0,
                                nanosecond: 0
                            )
                        )
                        let updatedPunch = Punch(
                            punchId: punch.punchId,
                            dateTime: updatedDateTime,
                            rateId: punch.rateId,
                            type: punch.type
                        )
                        onTimeSelected(updatedPunch)
                        onDismiss()
                    }) {
                        Text("OK")
                            .foregroundColor(.blue)
                            .bold()
                            .frame(maxWidth: .infinity)
                    }
                }
                .frame(height: 44)
                .padding(.vertical, 14)
            }
            .background(Color.white)
            .cornerRadius(12)
            .shadow(radius: 10)
            .frame(width: 300)
        }
    }
}
