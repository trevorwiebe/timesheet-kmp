//
//  GeneralDialog.swift
//  iosApp
//
//  Created by Trevor Wiebe on 4/30/25.
//  Copyright © 2025 orgName. All rights reserved.
//


import SwiftUI
import ComposeApp

struct GeneralDialog: View {
    
    var confirmText: String
    var onConfirm: () -> Void
    var dismissText: String
    var onDismiss: () -> Void
    var title: String?
    var message: String?
    
    var body: some View {
        ZStack {
            // Dim background
            Color.black.opacity(0.4)
                .edgesIgnoringSafeArea(.all)
                .onTapGesture {
                    onDismiss()
                }

            // Dialog content
            VStack(spacing: 16) {
                if let title = title {
                    Text(title)
                        .font(.headline)
                        .multilineTextAlignment(.center)
                        .padding(.top, 16)
                }

                if let message = message {
                    Text(message)
                        .font(.body)
                        .multilineTextAlignment(.center)
                        .padding([.leading, .trailing], 16)
                }

                Divider()

                HStack {
                    Button(action: onDismiss) {
                        Text(dismissText)
                            .foregroundColor(.red)
                            .frame(maxWidth: .infinity)
                    }

                    Divider()
                        .frame(height: 44)

                    Button(action: onConfirm) {
                        Text(confirmText)
                            .foregroundColor(.blue)
                            .bold()
                            .frame(maxWidth: .infinity)
                    }
                }
                .frame(height: 44)
                .background(Color.white)
            }
            .background(Color.white)
            .cornerRadius(12)
            .shadow(radius: 10)
            .frame(maxWidth: 300)
        }
    }
}
